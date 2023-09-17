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

        viewBinding?.let { vb ->
            initActivityResultLauncher(vb.pvViewFinder)
            setCameraButtonsListeners(
                vb.ivImageCaptionButton,
                vb.ivCameraSwitchButton,
                vb.pvViewFinder
            )
        }
    }

    override fun photoTaken(bitmap: Bitmap) {
        val croppedBitmap = bitmap.crop(
            activity?.windowManager?.defaultDisplay,
            viewBinding?.ovPoa?.poiFrameRectF
        )

        ImageCacheManager.poa = croppedBitmap

        activity
            ?.supportFragmentManager
            ?.beginTransaction()
            ?.replace(
                R.id.container,
                ImagePreviewFragment().apply {
                    arguments = bundleOf(IMAGE_TYPE to POA)
                }
            )
            ?.addToBackStack(null)
            ?.commit()
    }

    override fun analyseImage(luminosityIsFine: Boolean, boundingBox: RectF?, headIsStraight: Boolean?) {
//        with(viewBinding ?: return) {
//            tvMessage.visibility = if (!luminosityIsFine) {
//                View.VISIBLE
//            } else {
//                View.GONE
//            }
//        }

        val poaIsInFrame = (
                boundingBox?.let {
                    viewBinding?.ovPoa?.poiFrameRectF?.contains(it)
                } == true
                )
                && luminosityIsFine

        viewBinding?.ovPoa?.setDocumentIsInFrame(poaIsInFrame)
        viewBinding?.ovPoa?.poaBoundingBox = boundingBox
    }
}