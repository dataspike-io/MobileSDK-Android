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
        val vb = viewBinding ?: return

        with(vb) {
            initActivityResultLauncher(pvViewFinder)
            setCameraButtonsListeners(
                ivImageCaptionButton,
                ivCameraSwitchButton,
                pvViewFinder,
            )

            ivBackButton.setOnClickListener {
                parentFragmentManager.popBackStack()
            }
        }
    }

    override fun analyseImage(luminosityIsFine: Boolean, boundingBox: RectF?, headIsStraight: Boolean?) {
        //TODO return this
//        val stringId: Int
//        val textColorId: Int
//
//        with(viewBinding ?: return) {
//            if (!luminosityIsFine) {
//                stringId = R.string.photo_requires_more_light
//                textColorId = R.color.light_red
//            } else {
//                stringId = R.string.document_instructions
//                textColorId = R.color.white
//            }
//
//            with(tvBottomInstructionsText) {
//                text = getString(stringId)
//                setTextColor(ResourcesCompat.getColor(resources, textColorId, null))
//            }
//        }

        val poiIsInFrame = (
                boundingBox?.let {
                    viewBinding?.ovPoi?.poiFrameRectF?.contains(it)
                } == true
                )
                && luminosityIsFine

        viewBinding?.ovPoi?.setDocumentIsInFrame(poiIsInFrame)
        viewBinding?.ovPoi?.poiBoundingBox = boundingBox
    }

    override fun photoTaken(bitmap: Bitmap) {
        val croppedBitmap = bitmap.crop(
            activity?.windowManager?.defaultDisplay,
            viewBinding?.ovPoi?.poiFrameRectF
        )

        ImageCacheManager.putBitmapIntoCache(imageType ?: "", croppedBitmap)

        activity
            ?.supportFragmentManager
            ?.beginTransaction()
            ?.replace(
                R.id.container,
                ImagePreviewFragment().apply {
                    arguments = bundleOf(IMAGE_TYPE to imageType)
                }
            )
            ?.addToBackStack(null)
            ?.commit()
    }
}