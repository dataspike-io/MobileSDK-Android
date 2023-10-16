package io.dataspike.mobile_sdk.view.fragments

import android.Manifest
import android.graphics.Bitmap
import android.graphics.RectF
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.util.Size
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.OptIn
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.core.graphics.toRectF
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import com.google.mlkit.vision.face.FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE
import com.google.mlkit.vision.objects.ObjectDetection
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions
import io.dataspike.mobile_sdk.DocumentAnalysisListener
import io.dataspike.mobile_sdk.LivenessAnalysisListener
import io.dataspike.mobile_sdk.R
import io.dataspike.mobile_sdk.utils.Utils.displayMetrics
import io.dataspike.mobile_sdk.utils.Utils.rotate
import io.dataspike.mobile_sdk.utils.Utils.toByteArray
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

private const val TAG = "DataspikeSDK"
private const val CAMERA_PERMISSION = Manifest.permission.CAMERA
private const val MIN_HEAD_ROTATION = -10
private const val MAX_HEAD_ROTATION = 10
private const val MIN_LUMINOSITY = 80

internal abstract class BaseCameraFragment
    : BaseFragment(), DocumentAnalysisListener, LivenessAnalysisListener {

    private var imageCapture: ImageCapture? = null
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var activityResultLauncher: ActivityResultLauncher<String>
    private val imageAnalysis = ImageAnalysis.Builder()
        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
        .build()
    var cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cameraExecutor = Executors.newSingleThreadExecutor()
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    override fun analyseDocument(boundingBox: RectF) = Unit
    override fun analyseLiveness(
        luminosityIsFine: Boolean?,
        boundingBox: RectF?,
        livenessStatusStringId: Int?
    ) = Unit

    fun startCamera(previewView: PreviewView?) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener(
            {
                val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
                val preview = Preview.Builder().build().also {  preview ->
                    preview.setSurfaceProvider(previewView?.surfaceProvider)
                }

                imageCapture = ImageCapture.Builder()
                    .setTargetResolution(
                        Size(
                            displayMetrics.widthPixels,
                            displayMetrics.heightPixels
                        )
                    )
                    .build()

                try {
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        this,
                        cameraSelector,
                        preview,
                        imageCapture,
                        startImageAnalyzer(displayMetrics)
                    )
                } catch (e: Exception) {
                    Log.e(TAG, "Use case binding failed", e)
                }
            },
            ContextCompat.getMainExecutor(requireContext())
        )
    }

    @OptIn(ExperimentalGetImage::class)
    private fun startImageAnalyzer(displayMetrics: DisplayMetrics): ImageAnalysis {
        imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(requireContext())) { imageProxy ->
            val scaleX = (displayMetrics.widthPixels / imageProxy.height).toFloat()
            val scaleY = (displayMetrics.heightPixels / imageProxy.width).toFloat()

            if (this@BaseCameraFragment is LivenessVerificationFragment) {
                val buffer = imageProxy.planes[0].buffer
                val data = buffer.toByteArray()
                val pixels = data.map { it.toInt() and 0xFF }
                val luminosity = pixels.average()
                val faceDetectorOptions = FaceDetectorOptions.Builder()
                    .setPerformanceMode(PERFORMANCE_MODE_ACCURATE)
                    .build()
                val faceDetector = FaceDetection.getClient(faceDetectorOptions)

                faceDetector.process(
                    InputImage.fromMediaImage(
                        imageProxy.image ?: return@setAnalyzer,
                        imageProxy.imageInfo.rotationDegrees
                    )
                ).addOnCompleteListener {
                    val successfulResult = if (it.isSuccessful && it.result.isNotEmpty()) {
                        it.result[0]
                    } else {
                        null
                    }
                    val livenessStatusId = getLivenessStatusText(successfulResult, luminosity)
                    val box = successfulResult?.boundingBox?.toRectF() ?: RectF()
                    val scaledBoundingBox = RectF(
                        box.left * scaleX,
                        box.top * scaleY,
                        box.right * scaleX,
                        box.bottom * scaleY
                    )

                    analyseLiveness(
                        luminosityIsFine = luminosity >= MIN_LUMINOSITY,
                        boundingBox = scaledBoundingBox,
                        livenessStatusStringId = livenessStatusId,
                    )

                    imageProxy.close()
                }
            } else {
                val objectDetectorOptions = ObjectDetectorOptions.Builder()
                    .setDetectorMode(ObjectDetectorOptions.STREAM_MODE)
                    .build()
                val objectDetector = ObjectDetection.getClient(objectDetectorOptions)

                objectDetector.process(
                    InputImage.fromMediaImage(
                        imageProxy.image ?: return@setAnalyzer,
                        imageProxy.imageInfo.rotationDegrees
                    )
                ).addOnCompleteListener {
                    val successfulResult = if (it.isSuccessful && it.result.isNotEmpty()) {
                        it.result[0]
                    } else {
                        null
                    }

                    val box = successfulResult?.boundingBox?.toRectF() ?: RectF()
                    val scaledBoundingBox = RectF(
                        box.left * scaleX,
                        box.top * scaleY,
                        box.right * scaleX,
                        box.bottom * scaleY
                    )

                    analyseDocument(boundingBox = scaledBoundingBox)

                    imageProxy.close()
                }
            }
        }

        return imageAnalysis
    }

    private fun switchCamera(previewView: PreviewView?) {
        //TODO fix object detection on switch
        cameraSelector = when (cameraSelector) {
            CameraSelector.DEFAULT_FRONT_CAMERA -> {
                CameraSelector.DEFAULT_BACK_CAMERA
            }

            CameraSelector.DEFAULT_BACK_CAMERA -> {
                CameraSelector.DEFAULT_FRONT_CAMERA
            }

            else -> {
                CameraSelector.DEFAULT_FRONT_CAMERA
            }
        }

        startCamera(previewView)
    }

    private fun requestPermissions(activityResultLauncher: ActivityResultLauncher<String>) {
        activityResultLauncher.launch(CAMERA_PERMISSION)
    }

    private fun getLivenessStatusText(face: Face?, luminosity: Double): Int? = when {
            luminosity < MIN_LUMINOSITY -> { R.string.adjust_lighting_conditions }

            face == null -> { null }

            face.headEulerAngleX < MIN_HEAD_ROTATION -> { R.string.tilt_your_face_upward }

            face.headEulerAngleX > MAX_HEAD_ROTATION -> { R.string.tilt_your_face_downward }

            face.headEulerAngleY < MIN_HEAD_ROTATION -> { R.string.turn_your_face_slightly_left }

            face.headEulerAngleY > MAX_HEAD_ROTATION -> { R.string.turn_your_face_slightly_right }

            face.headEulerAngleZ < MIN_HEAD_ROTATION -> { R.string.tilt_your_face_to_the_right }

            face.headEulerAngleZ > MAX_HEAD_ROTATION -> { R.string.tilt_your_face_to_the_left }

            else -> { R.string.liveness_instructions_bad_title }
        }

    fun takePhoto() {
        val imageCapture = imageCapture ?: return

        imageCapture.takePicture(
            ContextCompat.getMainExecutor(requireContext()),
            object : ImageCapture.OnImageCapturedCallback() {
                @OptIn(androidx.camera.core.ExperimentalGetImage::class)
                override fun onCaptureSuccess(image: ImageProxy) {
                    super.onCaptureSuccess(image)

                    val bitmap = image.toBitmap().rotate(image.imageInfo.rotationDegrees.toFloat())

                    photoTaken(bitmap)
                    //TODO image.close() needed?
                    image.close()
                }
            }
        )
    }

    fun stopImageAnalyzer() {
        imageAnalysis.clearAnalyzer()
    }

    fun initActivityResultLauncher(viewFinder: PreviewView?) {
        activityResultLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { granted ->
            if (!granted) {
                makeToast("Permission request denied")
            } else {
                startCamera(viewFinder)
            }
        }

        requestPermissions(activityResultLauncher)
    }

    fun setCameraButtonsListeners(
        imageCaptionButton: View? = null,
        cameraSwitchButton: View,
        viewFinder: PreviewView,
    ) {
        imageCaptionButton?.setOnClickListener { takePhoto() }
        cameraSwitchButton.setOnClickListener { view ->
            view.animate().setDuration(200).rotation(view.rotation + 360f)

            switchCamera(viewFinder)
        }
    }

    abstract fun photoTaken(bitmap: Bitmap)
}