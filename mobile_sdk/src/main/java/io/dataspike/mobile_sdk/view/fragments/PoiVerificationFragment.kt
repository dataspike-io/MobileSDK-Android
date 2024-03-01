package io.dataspike.mobile_sdk.view.fragments

import android.graphics.Bitmap
import android.graphics.RectF
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import io.dataspike.mobile_sdk.R
import io.dataspike.mobile_sdk.databinding.FragmentPoiVerificationBinding
import io.dataspike.mobile_sdk.utils.crop
import io.dataspike.mobile_sdk.view.IMAGE_TYPE
import io.dataspike.mobile_sdk.view.POI_FRONT
import io.dataspike.mobile_sdk.view.view_models.BaseViewModel
import io.dataspike.mobile_sdk.view.view_models.DataspikeViewModelFactory

internal class PoiVerificationFragment : BaseCameraFragment() {

    private var viewBinding: FragmentPoiVerificationBinding? = null
    private val viewModel by viewModels<BaseViewModel> { DataspikeViewModelFactory() }
    private var imageType: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        imageType = arguments?.getString(IMAGE_TYPE)
        viewBinding = FragmentPoiVerificationBinding.inflate(
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
            initActivityResultLauncher()
            startCamera()
            cblCameraButtons.setup(
                takePhotoAction = ::takePhoto,
                switchCameraAction = ::switchCamera,
            )

            val stringResId = if (imageType == POI_FRONT) {
                R.string.front_photo_instructions
            } else {
                R.string.back_photo_instructions
            }

            hlHeader.setup(
                popBackStackAction = ::popBackStack,
                stringResId = stringResId,
            )
        }
    }

    override fun photoTaken(bitmap: Bitmap) {
        val croppedBitmap = bitmap.crop(viewBinding?.ovPoi?.poiFrameRectF)

        viewModel.putBitmapIntoCache(imageType ?: "", croppedBitmap)

        navigateToFragment(
            ImagePreviewFragment().apply {
                arguments = bundleOf(IMAGE_TYPE to imageType)
            },
            false
        )
    }

    override fun analyzeDocument(boundingBox: RectF) {
        with(viewBinding?.ovPoi ?: return) {
            setDocumentIsInFrame(poiFrameRectF?.contains(boundingBox) ?: false)
        }
    }
}