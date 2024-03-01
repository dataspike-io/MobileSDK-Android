package io.dataspike.mobile_sdk.view.custom_views

import android.content.Context
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.UnderlineSpan
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.annotation.ColorInt
import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.text.toSpannable
import io.dataspike.mobile_sdk.R
import io.dataspike.mobile_sdk.databinding.HeaderLayoutBinding
import io.dataspike.mobile_sdk.utils.setup
import io.dataspike.mobile_sdk.view.UIManager

internal class HeaderLayout @JvmOverloads constructor(
    context: Context,
    attrSet: AttributeSet? = null
): ConstraintLayout(context, attrSet) {

    private val viewBinding = HeaderLayoutBinding.inflate(
        LayoutInflater.from(context),
        this
    )
    private val palette = UIManager.getUiConfig().theme.palette
    private val typography = UIManager.getUiConfig().theme.typography

    fun setup(
        popBackStackAction: () -> Unit,
        @StringRes stringResId: Int,
        @ColorInt colorInt: Int? = ContextCompat.getColor(context, R.color.white_color),
    ) {
        with(viewBinding) {
            with(tvTopInstructions) {
                setup(
                    font = typography.header.font,
                    textSize = typography.header.textSize,
                    textColor = colorInt ?: typography.header.textColor,
                )
                text = getHeaderText(stringResId)
            }

            with(ivBackButton) {
                colorInt?.let { drawable.setTint(it) }
                setOnClickListener {
                    popBackStackAction.invoke()
                }
            }
        }
    }

    private fun getHeaderText(@StringRes stringResId: Int): Spannable {
        return if (
            stringResId == R.string.front_photo_instructions
            || stringResId == R.string.back_photo_instructions
        ) {
            val headerText = context.getString(stringResId)
            val spannable = SpannableString(headerText)
            val underlineSpan = UnderlineSpan()
            val colorSpan = ForegroundColorSpan(palette.mainColor)
            val wordToSpan = if (headerText.contains("front")) {
                "front"
            } else {
                "back"
            }

            val spanStartPosition = headerText.indexOf(wordToSpan)
            val spanEndPosition = spanStartPosition + wordToSpan.length

            spannable.setSpan(
                colorSpan,
                spanStartPosition,
                spanEndPosition,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            spannable.setSpan(
                underlineSpan,
                spanStartPosition,
                spanEndPosition,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            spannable
        } else {
            context.getString(stringResId).toSpannable()
        }
    }
}