package io.dataspike.mobile_sdk.view.custom_views

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.updatePadding
import io.dataspike.mobile_sdk.R
import io.dataspike.mobile_sdk.databinding.UploadResultLayoutBinding
import io.dataspike.mobile_sdk.utils.dpToPx
import io.dataspike.mobile_sdk.utils.setup
import io.dataspike.mobile_sdk.view.UIManager

internal const val UPLOAD_SUCCESSFUL = 0
internal const val UPLOAD_WITH_ERRORS = 1

internal class UploadResultLayout @JvmOverloads constructor(
    context: Context,
    attrSet: AttributeSet? = null
): ConstraintLayout(context, attrSet) {

    private val viewBinding = UploadResultLayoutBinding.inflate(
        LayoutInflater.from(context),
        this
    )
    private val palette = UIManager.getUiConfig().theme.palette
    private val typography = UIManager.getUiConfig().theme.typography

    fun setup(
        uploadStatus: Int,
        message: String? = null,
    ) {
        val isSuccessful = uploadStatus == UPLOAD_SUCCESSFUL

        with(viewBinding.tvUploadResult) {
            visibility = VISIBLE
            setup(
                font = typography.bodyTwo.font,
                textSize = typography.bodyTwo.textSize,
                textColor = if (isSuccessful) palette.successColor else palette.errorColor,
            )

            val drawable: Int
            val drawableColor: Int
            val backgroundColor: Int

            if (isSuccessful) {
                text = resources.getString(R.string.upload_successful)
                drawable = R.drawable.green_checkmark
                drawableColor = palette.successColor
                backgroundColor = palette.lighterSuccessColor
            } else {
                text = message
                drawable = R.drawable.red_x
                drawableColor = palette.errorColor
                backgroundColor = palette.lighterErrorColor
            }

            val backgroundDrawable = GradientDrawable().apply {
                shape = GradientDrawable.RECTANGLE
                color = ColorStateList.valueOf(backgroundColor)
                cornerRadius = dpToPx(8F)
            }

            setCompoundDrawablesWithIntrinsicBounds(
                ResourcesCompat.getDrawable(resources, drawable, null),
                null,
                null,
                null
            )
            compoundDrawables[0]?.setTint(drawableColor)
            background = backgroundDrawable
            updatePadding(
                dpToPx(4F).toInt(),
                dpToPx(6F).toInt(),
                dpToPx(8F).toInt(),
                dpToPx(6F).toInt()
            )
        }
    }
}