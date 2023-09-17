package io.dataspike.mobile_sdk.view.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.RectF
import android.os.Bundle
import android.util.Log
import android.util.Size
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.OptIn
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.core.graphics.toRectF
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import com.google.mlkit.vision.face.FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE
import com.google.mlkit.vision.objects.ObjectDetection
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions
import io.dataspike.mobile_sdk.ImageAnalysisListener
import io.dataspike.mobile_sdk.utils.Utils.rotate
import io.dataspike.mobile_sdk.utils.Utils.toByteArray
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.math.abs

private const val TAG = "DataspikeSDK"
private const val CAMERA_PERMISSION = Manifest.permission.CAMERA
private const val MAX_HEAD_ROTATION = 10
const val MINIMUM_LUMINOSITY = 80


internal abstract class BaseCameraFragment : BaseFragment(), ImageAnalysisListener {

    private var imageCapture: ImageCapture? = null
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var activityResultLauncher: ActivityResultLauncher<String>
    var cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cameraExecutor = Executors.newSingleThreadExecutor()
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    private fun startCamera(previewView: PreviewView?) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener(
            {
                val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
                val preview = Preview.Builder().build().also {  preview ->
                    preview.setSurfaceProvider(previewView?.surfaceProvider)
                }
                val width = activity?.windowManager?.defaultDisplay?.width ?: 0
                val height = activity?.windowManager?.defaultDisplay?.height ?: 0
                imageCapture = ImageCapture.Builder().setTargetResolution(Size(width, height)).build()

                try {
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        this,
                        cameraSelector,
                        preview,
                        imageCapture,
                        startImageAnalyzer()
                    )
                } catch (e: Exception) {
                    Log.e(TAG, "Use case binding failed", e)
                }
            },
            ContextCompat.getMainExecutor(requireContext())
        )
    }

    @OptIn(ExperimentalGetImage::class)
    private fun startImageAnalyzer(): ImageAnalysis {
        val width = activity?.windowManager?.defaultDisplay?.width ?: 0
        val height = activity?.windowManager?.defaultDisplay?.height ?: 0
        val imageAnalysis = ImageAnalysis.Builder()
//            .setTargetResolution(Size(width, height))
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()

        imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(requireContext())) { imageProxy ->
            val buffer = imageProxy.planes[0].buffer
            val data = buffer.toByteArray()
            val pixels = data.map { it.toInt() and 0xFF }
            val luminosity = pixels.average()

            if (this@BaseCameraFragment is LivenessVerificationFragment) {
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
                    val headIsStraight = successfulResult?.let { face ->
                        abs(face.headEulerAngleX) < MAX_HEAD_ROTATION
                        && abs(face.headEulerAngleY) < MAX_HEAD_ROTATION
                        && abs(face.headEulerAngleZ) < MAX_HEAD_ROTATION
                    }

                    val scaleX = (width / (imageProxy.image?.height ?: 1)).toFloat()
                    val scaleY = (height / (imageProxy.image?.width ?: 1)).toFloat()
                    val box = successfulResult?.boundingBox?.toRectF() ?: RectF()
                    val scaledBoundingBox = RectF(
                        box.left * 1.4f,
                        box.top * scaleY * 1.1f,
                        box.right * scaleX * 1.3f,
                        box.bottom * scaleY
                    )

                    analyseImage(
                        luminosity >= MINIMUM_LUMINOSITY,
                        scaledBoundingBox,
                        headIsStraight,
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

                    val scaleX = (width / (imageProxy.image?.height ?: 1)).toFloat()
                    val scaleY = (height / (imageProxy.image?.width ?: 1)).toFloat()
                    val box = successfulResult?.boundingBox?.toRectF() ?: RectF()
                    val scaledBoundingBox = RectF(
                        box.left,
                        box.top * scaleY,
                        box.right * scaleX * 1.4f,
                        box.bottom * scaleY
                    )

                    analyseImage(
                        luminosity >= MINIMUM_LUMINOSITY,
                        scaledBoundingBox
                    )

                    imageProxy.close()
                }
            }
        }

        return imageAnalysis
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

                //TODO
                override fun onError(exception: ImageCaptureException) {
                    super.onError(exception)
                }
            })
    }

    private fun switchCamera(previewView: PreviewView?) {

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

    fun initActivityResultLauncher(viewFinder: PreviewView) {
        activityResultLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { granted ->
            if (!granted) {
                Toast.makeText(
                    requireContext(), "Permission request denied", Toast.LENGTH_SHORT
                ).show()
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

    fun cameraPermissionGranted() = ContextCompat.checkSelfPermission(
        requireContext(), CAMERA_PERMISSION
    ) == PackageManager.PERMISSION_GRANTED

    abstract fun photoTaken(bitmap: Bitmap)
}