package io.dataspike.mobile_sdk.view.fragments

import android.graphics.Bitmap
import android.graphics.RectF
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.camera.core.CameraSelector
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.viewModels
import io.dataspike.mobile_sdk.R
import io.dataspike.mobile_sdk.data.image_caching.ImageCacheManager
import io.dataspike.mobile_sdk.databinding.FragmentLivenessVerificationBinding
import io.dataspike.mobile_sdk.domain.VerificationManager
import io.dataspike.mobile_sdk.domain.models.UploadImageState
import io.dataspike.mobile_sdk.utils.Utils.launchInMain
import io.dataspike.mobile_sdk.view.LIVENESS
import io.dataspike.mobile_sdk.view.view_models.DataspikeViewModelFactory
import io.dataspike.mobile_sdk.view.view_models.LivenessVerificationViewModel

internal class LivenessVerificationFragment : BaseCameraFragment() {

    private var viewBinding: FragmentLivenessVerificationBinding? = null
    private val viewModel: LivenessVerificationViewModel by viewModels {
        DataspikeViewModelFactory()
    }
    private var needToStartPhotoTimer = true
    private var cameraTimer = object : CountDownTimer(2000, 1000) {
        override fun onTick(millisUntilFinished: Long) = Unit

        override fun onFinish() {
            takePhoto()
        }
    }
    private var livenessSuccessfulTimer = object : CountDownTimer(
        2000,
        1000
    ) {
        override fun onTick(millisUntilFinished: Long) = Unit

        override fun onFinish() {
            val fragmentToNavigateTo = if (VerificationManager.checks.poaIsRequired) {
                POAChooserFragment()
            } else {
                VerificationCompleteFragment()
            }

            navigateToFragment(fragmentToNavigateTo)
        }
    }

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

        collectUploadImageFlow()
        collectLoading(viewModel, viewBinding?.llLoadingView?.root)

        cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA

        with(viewBinding ?: return) {
            clCameraButtons.ivImageCaptionButton.visibility = View.GONE

            initActivityResultLauncher(pvViewFinder)
            setCameraButtonsListeners(
                cameraSwitchButton = clCameraButtons.ivCameraSwitchButton,
                viewFinder = pvViewFinder,
            )

            with(clWhiteTextHeaderLayout) {
                with(tvTopInstructions) {
                    text = context?.applicationContext?.getString(
                        R.string.liveness_instructions_bad_title
                    )
                    setTextColor(
                        ResourcesCompat.getColor(
                        resources,
                            R.color.light_red,
                            null
                        )
                    )
                }
                ivBackButton.setOnClickListener {
                    parentFragmentManager.popBackStack()
                }
            }
        }
    }

    override fun photoTaken(bitmap: Bitmap) {
        stopImageAnalyzer()
        ImageCacheManager.putBitmapIntoCache(LIVENESS, bitmap)

        with(viewBinding ?: return) {
            ivImagePreview.visibility = View.VISIBLE
            tvLivenessInstructionsText.visibility = View.GONE
            clCameraButtons.root.visibility = View.GONE
            ivImagePreview.setImageBitmap(
                ImageCacheManager.getBitmapFromCache(LIVENESS)
            )
        }

        viewModel.uploadImage(
            requireContext().getExternalFilesDir(null)?.absolutePath ?: "",
            LIVENESS
        )
    }

    override fun analyseLiveness(
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
                cameraTimer.start()
                needToStartPhotoTimer = false
            }
        } else {
            cameraTimer.cancel()
            needToStartPhotoTimer = true
        }

        with(viewBinding ?: return) {
            //TODO fix not attached to a context
            with(clWhiteTextHeaderLayout.tvTopInstructions) {
                text = context?.applicationContext?.getString(stringId)
                setTextColor(ResourcesCompat.getColor(resources, textColorId, null))
            }
            ovLiveness.setFaceIsInFrame(faceIsInFrame)
            ovLiveness.livenessBoundingBox = boundingBox
        }
    }
    private fun collectUploadImageFlow() {
        launchInMain {
            viewModel.imageUploadedFlow.collect { uploadImageState ->
                if (uploadImageState is UploadImageState.UploadImageSuccess) {
                    with(viewBinding ?: return@collect) {
                        tvLivenessSuccessful.visibility = View.VISIBLE
                        clWhiteTextHeaderLayout.tvTopInstructions.text =
                            requireContext().getString(R.string.completed)
                    }

                    livenessSuccessfulTimer.start()
                } else {
                    with(viewBinding ?: return@collect) {
                        startCamera(pvViewFinder)
                        clCameraButtons.root.visibility = View.VISIBLE
                        ivImagePreview.visibility = View.GONE
                        tvLivenessInstructionsText.visibility = View.VISIBLE
                    }
                }
            }
        }
    }
}