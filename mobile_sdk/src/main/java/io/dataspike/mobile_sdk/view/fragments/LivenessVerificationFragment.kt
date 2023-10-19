package io.dataspike.mobile_sdk.view.fragments

import android.graphics.Bitmap
import android.graphics.RectF
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.OptIn
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageProxy
import androidx.core.graphics.toRectF
import androidx.fragment.app.viewModels
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import io.dataspike.mobile_sdk.R
import io.dataspike.mobile_sdk.databinding.FragmentLivenessVerificationBinding
import io.dataspike.mobile_sdk.domain.models.UploadImageState
import io.dataspike.mobile_sdk.utils.flipHorizontally
import io.dataspike.mobile_sdk.utils.launchInMain
import io.dataspike.mobile_sdk.utils.toByteArray
import io.dataspike.mobile_sdk.view.LIVENESS
import io.dataspike.mobile_sdk.view.view_models.DataspikeViewModelFactory
import io.dataspike.mobile_sdk.view.view_models.LivenessVerificationViewModel

private const val MIN_HEAD_ROTATION = -10
private const val MAX_HEAD_ROTATION = 10
private const val MIN_LUMINOSITY = 80

internal class LivenessVerificationFragment : BaseCameraFragment() {

    private var viewBinding: FragmentLivenessVerificationBinding? = null
    private val viewModel: LivenessVerificationViewModel by viewModels {
        DataspikeViewModelFactory()
    }
    private var needToStartPhotoTimer = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentLivenessVerificationBinding.inflate(
            inflater,
            container,
            false
        )

        return viewBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(viewBinding ?: return) {
            setPreviewView(pvViewFinder)
            collectUploadImageFlow()
            collectTakePhotoFlow()
            collectNavigateToFragmentFlow()
            collectLoading(viewModel, llLoadingView.root)
            setCameraSelector(CameraSelector.DEFAULT_FRONT_CAMERA)
            initActivityResultLauncher()
            setScreenUiState()
        }
    }

    override fun photoTaken(bitmap: Bitmap) {
        stopImageAnalyzer()

        viewModel.putBitmapIntoCache(LIVENESS, bitmap)

        with(viewBinding ?: return) {
            lpivImagePreview.setup(bitmap = bitmap.flipHorizontally())
            tvLivenessInstructionsText.visibility = View.GONE
            cblCameraButtons.visibility = View.GONE
        }

        viewModel.uploadImage(LIVENESS)
    }

    @OptIn(ExperimentalGetImage::class)
    override fun process(
        imageProxy: ImageProxy,
        scaleX: Float,
        scaleY: Float
    ) {
        val buffer = imageProxy.planes[0].buffer
        val data = buffer.toByteArray()
        val pixels = data.map { it.toInt() and 0xFF }
        val luminosity = pixels.average()
        val faceDetectorOptions = FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
            .build()
        val faceDetector = FaceDetection.getClient(faceDetectorOptions)

        faceDetector.process(
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
            val livenessStatusId = getLivenessStatusText(successfulResult, luminosity)
            val box = successfulResult?.boundingBox?.toRectF() ?: RectF()
            val scaledBoundingBox = RectF(
                box.left * scaleX,
                box.top * scaleY,
                box.right * scaleX,
                box.bottom * scaleY
            )

            analyzeLiveness(
                luminosityIsFine = luminosity >= MIN_LUMINOSITY,
                boundingBox = scaledBoundingBox,
                livenessStatusStringId = livenessStatusId,
            )

            imageProxy.close()
        }
    }
    override fun analyzeLiveness(
        luminosityIsFine: Boolean?,
        boundingBox: RectF?,
        livenessStatusStringId: Int?,
    ) {
        var stringId: Int = livenessStatusStringId ?: R.string.liveness_instructions_bad_title
        var textColorId: Int = R.color.light_red
        val faceIsInFrame = (
                boundingBox?.let {
                    viewBinding?.ovLiveness?.livenessFrameRectF?.contains(it)
                } == true
                )
                && livenessStatusStringId == R.string.liveness_instructions_bad_title

        if (faceIsInFrame) {
            stringId = R.string.liveness_instructions_title
            textColorId = R.color.white

            if (needToStartPhotoTimer) {
                viewModel.startCameraTimer()
                needToStartPhotoTimer = false
            }
        } else {
            viewModel.cancelCameraTimer()
            needToStartPhotoTimer = true
        }

        with(viewBinding ?: return) {
            hlHeader.setup(
                popBackStackAction = ::popBackStack,
                stringResId = stringId,
                colorResId = textColorId,
            )
            ovLiveness.setFaceIsInFrame(faceIsInFrame)
        }
    }

    private fun setScreenUiState() {
        with(viewBinding ?: return) {
            lpivImagePreview.visibility = View.GONE
            tvLivenessSuccessful.visibility = View.GONE
            cblCameraButtons.visibility = View.VISIBLE
            tvLivenessInstructionsText.visibility = View.VISIBLE
            cblCameraButtons.setup(
                takePhotoAction = ::takePhoto,
                switchCameraAction = ::switchCamera,
                showCaptionButton = false,
            )
            hlHeader.setup(
                popBackStackAction = ::popBackStack,
                stringResId = R.string.liveness_instructions_bad_title,
                colorResId = R.color.light_red,
            )
        }
    }

    private fun getLivenessStatusText(face: Face?, luminosity: Double): Int? {
        return when {
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
    }

    private fun collectUploadImageFlow() {
        launchInMain {
            viewModel.imageUploadedFlow.collect { uploadImageState ->
                if (uploadImageState is UploadImageState.UploadImageSuccess) {
                    with(viewBinding ?: return@collect) {
                        tvLivenessSuccessful.visibility = View.VISIBLE
                        hlHeader.setup(
                            popBackStackAction = ::popBackStack,
                            stringResId = R.string.completed,
                            colorResId = R.color.white,
                        )
                    }

                    viewModel.startLivenessSuccessfulTimer()
                } else {
                    setScreenUiState()
                    startCamera()
                }
            }
        }
    }

    private fun collectTakePhotoFlow() {
        launchInMain {
            viewModel.takePhotoFlow.collect {
                takePhoto()
            }
        }
    }

    private fun collectNavigateToFragmentFlow() {
        launchInMain {
            viewModel.navigateToFragmentFlow.collect { fragment ->
                navigateToFragment(fragment, false)
            }
        }
    }
}