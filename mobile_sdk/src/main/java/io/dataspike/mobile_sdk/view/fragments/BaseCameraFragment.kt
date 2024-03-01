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
import com.google.mlkit.vision.objects.ObjectDetection
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions
import io.dataspike.mobile_sdk.R
import io.dataspike.mobile_sdk.utils.displayMetrics
import io.dataspike.mobile_sdk.utils.rotate
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

private const val CAMERA_PERMISSION = Manifest.permission.CAMERA

internal abstract class BaseCameraFragment
    : BaseFragment(), DocumentAnalysisListener, LivenessAnalysisListener {

    private var viewFinder: PreviewView? = null
    private var imageCapture: ImageCapture? = null
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var activityResultLauncher: ActivityResultLauncher<String>
    private lateinit var imageAnalysis: ImageAnalysis
    private var cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cameraExecutor = Executors.newSingleThreadExecutor()
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    override fun analyzeDocument(boundingBox: RectF) = Unit
    override fun analyzeLiveness(
        luminosityIsFine: Boolean?,
        boundingBox: RectF?,
        livenessStatusStringId: Int?
    ) = Unit

    private fun startImageAnalyzer(displayMetrics: DisplayMetrics): ImageAnalysis {
        imageAnalysis = ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()

        imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(requireContext())) { imageProxy ->
            val scaleX = (displayMetrics.widthPixels / imageProxy.height).toFloat()
            val scaleY = (displayMetrics.heightPixels / imageProxy.width).toFloat()

            process(imageProxy, scaleX, scaleY)
        }

        return imageAnalysis
    }

    private fun requestPermissions(activityResultLauncher: ActivityResultLauncher<String>) {
        activityResultLauncher.launch(CAMERA_PERMISSION)
    }

    protected fun stopImageAnalyzer() {
        imageAnalysis.clearAnalyzer()
    }

    protected fun initActivityResultLauncher() {
        activityResultLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { granted ->
            if (!granted) {
                makeToast(getString(R.string.permission_request_denied))
            } else {
                startCamera()
            }
        }

        requestPermissions(activityResultLauncher)
    }

    protected fun setCameraSelector(cameraSelector: CameraSelector) {
        this.cameraSelector = cameraSelector
    }

    protected fun setPreviewView(previewView: PreviewView?) {
        viewFinder = previewView
    }

    @OptIn(ExperimentalGetImage::class)
    protected open fun process(
        imageProxy: ImageProxy,
        scaleX: Float,
        scaleY: Float
    ) {
        val objectDetectorOptions = ObjectDetectorOptions.Builder()
            .setDetectorMode(ObjectDetectorOptions.STREAM_MODE)
            .build()
        val objectDetector = ObjectDetection.getClient(objectDetectorOptions)

        objectDetector.process(
            InputImage.fromMediaImage(
                imageProxy.image ?: return,
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

            analyzeDocument(boundingBox = scaledBoundingBox)

            imageProxy.close()
        }
    }

    protected fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener(
            {
                val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
                val preview = Preview.Builder().build().also {  preview ->
                    preview.setSurfaceProvider(viewFinder?.surfaceProvider)
                }

                imageCapture = ImageCapture.Builder()
                    .setTargetResolution(
                        Size(
                            displayMetrics.widthPixels,
                            displayMetrics.heightPixels
                        )
                    )
                    .build()

                kotlin.runCatching {
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        this,
                        cameraSelector,
                        preview,
                        imageCapture,
                        startImageAnalyzer(displayMetrics)
                    )
                }.onFailure { throwable ->
                    Log.e("Dataspike", "Use case binding failed", throwable)
                }
            },
            ContextCompat.getMainExecutor(requireContext())
        )
    }

    protected fun takePhoto() {
        (imageCapture ?: return).takePicture(
            ContextCompat.getMainExecutor(requireContext()),
            object : ImageCapture.OnImageCapturedCallback() {

                @OptIn(androidx.camera.core.ExperimentalGetImage::class)
                override fun onCaptureSuccess(image: ImageProxy) {
                    super.onCaptureSuccess(image)

                    val bitmap = image.toBitmap().rotate(image.imageInfo.rotationDegrees.toFloat())

                    photoTaken(bitmap)

                    image.close()
                }
            }
        )
    }

    protected fun switchCamera() {
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

        stopImageAnalyzer()
        startCamera()
    }

    protected abstract fun photoTaken(bitmap: Bitmap)
}