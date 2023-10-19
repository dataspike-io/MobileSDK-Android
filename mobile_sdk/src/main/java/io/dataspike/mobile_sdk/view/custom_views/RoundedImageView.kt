package io.dataspike.mobile_sdk.view.custom_views

import android.content.Context
import android.graphics.Bitmap
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import io.dataspike.mobile_sdk.utils.dpToPx

internal class RoundedImageView(
    context: Context,
    attrSet: AttributeSet? = null
): AppCompatImageView(context, attrSet) {

    fun setImageDrawableFromBitmap(bitmap: Bitmap?) {
        val roundedDrawable = RoundedBitmapDrawableFactory.create(resources, bitmap).apply {
            cornerRadius = dpToPx(14f)
        }

        setImageDrawable(roundedDrawable)
    }
}