package io.dataspike.mobile_sdk.view.custom_views

import android.content.Context
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.UnderlineSpan
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.text.toSpannable
import io.dataspike.mobile_sdk.R
import io.dataspike.mobile_sdk.databinding.HeaderLayoutBinding

internal class HeaderLayout @JvmOverloads constructor(
    context: Context,
    attrSet: AttributeSet? = null
): ConstraintLayout(context, attrSet) {

    private val viewBinding = HeaderLayoutBinding.inflate(
        LayoutInflater.from(context),
        this
    )

    fun setup(
        popBackStackAction: () -> Unit,
        @StringRes stringResId: Int,
        @ColorRes colorResId: Int? = null,
    ) {
        with(viewBinding) {
            with(tvTopInstructions) {
                colorResId?.let { setTextColor(ContextCompat.getColor(context, it)) }
                text = getHeaderText(stringResId)
            }

            with(ivBackButton) {
                colorResId?.let { setColorFilter(ContextCompat.getColor(context, it)) }

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
            val colorSpan = ForegroundColorSpan(ContextCompat.getColor(context, R.color.ds_purple))
            val wordToSpan = if (headerText.contains("front")) {
                "front"
            } else {
                "back"
            }

            val spanStartPosition = headerText.indexOf(wordToSpan)
            val endSpanPosition = spanStartPosition + wordToSpan.length

            spannable.setSpan(
                colorSpan,
                spanStartPosition,
                endSpanPosition,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            spannable.setSpan(
                underlineSpan,
                spanStartPosition,
                endSpanPosition,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            spannable
        } else {
            context.getString(stringResId).toSpannable()
        }
    }
}