package io.dataspike.mobile_sdk.view.custom_views

import android.content.Context
import android.graphics.Bitmap
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView

internal class LivenessPreviewImageView(
    context: Context,
    attrSet: AttributeSet? = null
): AppCompatImageView(context, attrSet) {

    fun setup(bitmap: Bitmap?) {
        visibility = VISIBLE
        setImageBitmap(bitmap)
    }
}