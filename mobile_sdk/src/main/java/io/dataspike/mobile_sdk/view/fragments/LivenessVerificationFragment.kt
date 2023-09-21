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
import androidx.core.os.bundleOf
import io.dataspike.mobile_sdk.R
import io.dataspike.mobile_sdk.data.image_caching.ImageCacheManager
import io.dataspike.mobile_sdk.databinding.FragmentLivenessVerificationBinding
import io.dataspike.mobile_sdk.view.IMAGE_TYPE
import io.dataspike.mobile_sdk.view.LIVENESS

internal class LivenessVerificationFragment : BaseCameraFragment() {

    private var viewBinding: FragmentLivenessVerificationBinding? = null
    private var needToStartPhotoTimer = true
    private var timer = object : CountDownTimer(2000, 2000) {
        override fun onTick(millisUntilFinished: Long) = Unit

        override fun onFinish() {
            with(viewBinding ?: return) {
                tvLivenessSuccessful.visibility = View.VISIBLE
                tvTopInstructions.text = requireContext().getString(R.string.completed)
                tvBottomInstructionsText.visibility = View.GONE
            }

            takePhoto()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentLivenessVerificationBinding.inflate(inflater, container, false)
        return viewBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val vb = viewBinding ?: return

        cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA

        with(vb) {
            initActivityResultLauncher(pvViewFinder)
            setCameraButtonsListeners(
                cameraSwitchButton = ivCameraSwitchButton,
                viewFinder = pvViewFinder,
            )

            ivBackButton.setOnClickListener {
                parentFragmentManager.popBackStack()
            }
        }
    }

    override fun photoTaken(bitmap: Bitmap) {
        ImageCacheManager.putBitmapIntoCache(LIVENESS, bitmap)

        activity
            ?.supportFragmentManager
            ?.beginTransaction()
            ?.replace(
                R.id.container,
                ImagePreviewFragment().apply {
                    arguments = bundleOf(IMAGE_TYPE to LIVENESS)
                }
            )
            ?.addToBackStack(null)
            ?.commit()
    }

    override fun analyseImage(luminosityIsFine: Boolean, boundingBox: RectF?, headIsStraight: Boolean?) {
        val vb = viewBinding ?: return
        var stringId: Int = R.string.liveness_instructions_bad_title
        var textColorId: Int = R.color.light_red

        if (!luminosityIsFine) {
            stringId = R.string.photo_requires_more_light
            textColorId = R.color.light_red
        }

        val faceIsInFrame = (
                boundingBox?.let {
                    viewBinding?.ovLiveness?.livenessFrameRectF?.contains(it)
                } == true
                )
                && headIsStraight == true
                && luminosityIsFine

        if (faceIsInFrame) {
            stringId = R.string.liveness_instructions_title
            textColorId = R.color.white

            if (needToStartPhotoTimer) {
                timer.start()
                needToStartPhotoTimer = false
            }
        } else {
            timer.cancel()
            needToStartPhotoTimer = true
        }

        with(vb.tvTopInstructions) {
            //TODO fix
//            text = requireContext().getString(stringId)
            setTextColor(ResourcesCompat.getColor(resources, textColorId, null))
        }


        viewBinding?.ovLiveness?.setFaceIsInFrame(faceIsInFrame)
        viewBinding?.ovLiveness?.livenessBoundingBox = boundingBox
    }
}