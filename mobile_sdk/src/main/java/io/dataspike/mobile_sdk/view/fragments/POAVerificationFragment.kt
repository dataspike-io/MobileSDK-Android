package io.dataspike.mobile_sdk.view.fragments

import android.graphics.Bitmap
import android.graphics.RectF
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import io.dataspike.mobile_sdk.R
import io.dataspike.mobile_sdk.data.image_caching.ImageCacheManager
import io.dataspike.mobile_sdk.databinding.FragmentPoaVerificationBinding
import io.dataspike.mobile_sdk.utils.Utils.crop
import io.dataspike.mobile_sdk.view.IMAGE_TYPE
import io.dataspike.mobile_sdk.view.POA

internal class POAVerificationFragment : BaseCameraFragment() {

    private var viewBinding: FragmentPoaVerificationBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentPoaVerificationBinding.inflate(inflater, container, false)
        return viewBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(viewBinding ?: return) {
            initActivityResultLauncher(pvViewFinder)
            setCameraButtonsListeners(
                clCameraButtons.ivImageCaptionButton,
                clCameraButtons.ivCameraSwitchButton,
                pvViewFinder
            )

            with(clWhiteTextHeaderLayout) {
                tvTopInstructions.text = requireContext().getString(
                    R.string.place_the_document_in_frame_and_take_a_photo
                )
                ivBackButton.setOnClickListener {
                    parentFragmentManager.popBackStack()
                }
            }
        }
    }

    override fun photoTaken(bitmap: Bitmap) {
        val croppedBitmap = bitmap.crop(viewBinding?.ovPoa?.poiFrameRectF)

        ImageCacheManager.putBitmapIntoCache(POA, croppedBitmap)

        navigateToFragment(
            ImagePreviewFragment().apply {
                arguments = bundleOf(IMAGE_TYPE to POA)
            },
            false
        )
    }

    override fun analyseDocument(boundingBox: RectF) {
        val poaIsInFrame = viewBinding?.ovPoa?.poaFrameRectF?.contains(boundingBox) ?: false

        viewBinding?.ovPoa?.setDocumentIsInFrame(poaIsInFrame)
        viewBinding?.ovPoa?.poaBoundingBox = boundingBox
    }
}