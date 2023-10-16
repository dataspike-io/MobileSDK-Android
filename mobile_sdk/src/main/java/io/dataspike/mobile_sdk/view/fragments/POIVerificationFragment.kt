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
import io.dataspike.mobile_sdk.databinding.FragmentPoiVerificationBinding
import io.dataspike.mobile_sdk.utils.Utils.crop
import io.dataspike.mobile_sdk.view.IMAGE_TYPE

internal class POIVerificationFragment : BaseCameraFragment() {

    private var viewBinding: FragmentPoiVerificationBinding? = null

    private var imageType: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        imageType = arguments?.getString(IMAGE_TYPE)
        viewBinding = FragmentPoiVerificationBinding.inflate(inflater, container, false)
        return viewBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //TODO recreate state with taken image
        with(viewBinding ?: return) {
            initActivityResultLauncher(pvViewFinder)
            setCameraButtonsListeners(
                clCameraButtons.ivImageCaptionButton,
                clCameraButtons.ivCameraSwitchButton,
                pvViewFinder,
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
        val croppedBitmap = bitmap.crop(viewBinding?.ovPoi?.poiFrameRectF)

        ImageCacheManager.putBitmapIntoCache(imageType ?: "", croppedBitmap)

        navigateToFragment(
            ImagePreviewFragment().apply {
                arguments = bundleOf(IMAGE_TYPE to imageType)
            },
            false
        )
    }

    override fun analyseDocument(boundingBox: RectF) {
        val poiIsInFrame = viewBinding?.ovPoi?.poiFrameRectF?.contains(boundingBox) ?: false

        viewBinding?.ovPoi?.setDocumentIsInFrame(poiIsInFrame)
        viewBinding?.ovPoi?.poiBoundingBox = boundingBox
    }
}