package io.dataspike.mobile_sdk.view.custom_views

import android.content.Context
import android.graphics.Canvas
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.graphics.Path
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import io.dataspike.mobile_sdk.utils.getFont

internal class DashedUnderlineTextView(
    context: Context,
    attrs: AttributeSet? = null
): AppCompatTextView(context, attrs) {

    fun setup(
        font: String,
        textSize: Float,
        textColor: Int,
    ) {
        background = DashedUnderlineDrawable(textColor)
        setTextColor(textColor)
        this.textSize = textSize
        typeface = getFont(context, font)
    }

    inner class DashedUnderlineDrawable(mainColor: Int) : GradientDrawable() {

        private val paint = Paint().apply {
            color = mainColor
            style = Paint.Style.STROKE
            strokeWidth = 5f
            pathEffect = DashPathEffect(floatArrayOf(15f, 10f), 0f)
        }

        override fun draw(canvas: Canvas) {
            val path = Path()
            path.moveTo(bounds.left.toFloat(), bounds.bottom.toFloat())
            path.lineTo(bounds.right.toFloat(), bounds.bottom.toFloat())
            canvas.drawPath(path, paint)
        }
    }
}