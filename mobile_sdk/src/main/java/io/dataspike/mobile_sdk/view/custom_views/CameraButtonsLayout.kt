package io.dataspike.mobile_sdk.view.custom_views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import io.dataspike.mobile_sdk.databinding.CameraButtonsLayoutBinding

internal class CameraButtonsLayout @JvmOverloads constructor(
    context: Context,
    attrSet: AttributeSet? = null
): ConstraintLayout(context, attrSet) {

    private val viewBinding = CameraButtonsLayoutBinding.inflate(
        LayoutInflater.from(context),
        this
    )

    fun setup(
        takePhotoAction: () -> Unit,
        switchCameraAction: () -> Unit,
        showCaptionButton: Boolean = true,
    ) {
        with(viewBinding) {
            if (showCaptionButton) {
                ivImageCaptionButton.visibility = VISIBLE
                ivImageCaptionButton.setOnClickListener { takePhotoAction.invoke() }
            }

            ivCameraSwitchButton.setOnClickListener { view ->
                view.animate().setDuration(200).rotation(view.rotation + 360f)
                switchCameraAction.invoke()
            }
        }
    }
}