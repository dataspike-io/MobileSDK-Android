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
import io.dataspike.mobile_sdk.databinding.FragmentPoaVerificationBinding
import io.dataspike.mobile_sdk.utils.crop
import io.dataspike.mobile_sdk.view.IMAGE_TYPE
import io.dataspike.mobile_sdk.view.POA
import io.dataspike.mobile_sdk.view.view_models.BaseViewModel
import io.dataspike.mobile_sdk.view.view_models.DataspikeViewModelFactory

internal class PoaVerificationFragment : BaseCameraFragment() {

    private var viewBinding: FragmentPoaVerificationBinding? = null
    private val viewModel by viewModels<BaseViewModel> { DataspikeViewModelFactory() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentPoaVerificationBinding.inflate(
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
            hlHeader.setup(
                popBackStackAction = ::popBackStack,
                stringResId = R.string.place_the_document_in_frame_and_take_a_photo,
                colorResId = R.color.white,
            )
        }
    }

    override fun photoTaken(bitmap: Bitmap) {
        val croppedBitmap = bitmap.crop(viewBinding?.ovPoa?.poiFrameRectF)

        viewModel.putBitmapIntoCache(POA, croppedBitmap)

        navigateToFragment(
            ImagePreviewFragment().apply {
                arguments = bundleOf(IMAGE_TYPE to POA)
            },
            false
        )
    }

    override fun analyzeDocument(boundingBox: RectF) {
        with(viewBinding?.ovPoa ?: return) {
            setDocumentIsInFrame(poaFrameRectF?.contains(boundingBox) ?: false)
        }
    }
}