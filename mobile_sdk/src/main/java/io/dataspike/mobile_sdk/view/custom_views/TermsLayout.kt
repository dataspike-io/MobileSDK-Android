package io.dataspike.mobile_sdk.view.custom_views

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.net.Uri
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.core.os.bundleOf
import androidx.core.text.toSpannable
import io.dataspike.mobile_sdk.R
import io.dataspike.mobile_sdk.databinding.TermsLayoutBinding
import io.dataspike.mobile_sdk.utils.setup
import io.dataspike.mobile_sdk.view.UIManager


private const val TERMS_AND_CONDITIONS_LINK = "https://dataspike.io/terms?lang=en"
private const val PERSONAL_DATA_LINK = "https://dataspike.io/privacy?lang=en"

internal class TermsLayout @JvmOverloads constructor(
    context: Context,
    attrSet: AttributeSet? = null
): ConstraintLayout(context, attrSet) {

    private val viewBinding = TermsLayoutBinding.inflate(
        LayoutInflater.from(context),
        this
    )
    private var onBothBoxesAreCheckedListener: ((Boolean) -> Unit)? = null
    private val palette = UIManager.getUiConfig().theme.palette
    private val typography = UIManager.getUiConfig().theme.typography

    init {
        setupTextViews()
        setupListeners()
    }

    private fun setupTextViews() {
        with(viewBinding) {
            with(tvTermsAndConditions) {
                setup(
                    font = typography.bodyTwo.font,
                    textSize = typography.bodyTwo.textSize,
                    textColor = typography.bodyTwo.textColor,
                )
                text = getTermsText(R.string.i_accept_terms_and_conditions)
                movementMethod = LinkMovementMethod.getInstance()
                highlightColor = ContextCompat.getColor(context, R.color.transparent)
            }

            with(tvPersonalData) {
                setup(
                    font = typography.bodyTwo.font,
                    textSize = typography.bodyTwo.textSize,
                    textColor = typography.bodyTwo.textColor,
                )
                text = getTermsText(R.string.i_agree_to_processing_my_personal_data)
                movementMethod = LinkMovementMethod.getInstance()
                highlightColor = ContextCompat.getColor(context, R.color.transparent)
            }
        }
    }

    private fun getTermsText(@StringRes stringResId: Int): Spannable {
        return if (
            stringResId == R.string.i_accept_terms_and_conditions
            || stringResId == R.string.i_agree_to_processing_my_personal_data
        ) {
            val termsText = context.getString(stringResId)
            val spannable = SpannableString(termsText)
            val isTermsAndConditions = termsText.contains("terms", true)
            val textToSpan = if (isTermsAndConditions) {
                "Terms and Conditions"
            } else {
                "Personal Data"
            }
            val spanStartPosition = termsText.indexOf(textToSpan)
            val spanEndPosition = spanStartPosition + textToSpan.length

            spannable.setSpan(
                object : ClickableSpan() {

                    override fun updateDrawState(ds: TextPaint) {
                        super.updateDrawState(ds)
                        ds.isUnderlineText = false
                        ds.color = palette.mainColor
                    }

                    override fun onClick(widget: View) {
                        openLink(isTermsAndConditions)
                    }
                },
                spanStartPosition,
                spanEndPosition,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            spannable
        } else {
            context.getString(stringResId).toSpannable()
        }
    }

    @SuppressLint("RestrictedApi")
    private fun setupListeners() {
        val onCheckedChangeListener: (Boolean, Boolean) -> Unit =
            { termsAndConditionsIsChecked, personalDataIsChecked ->
                onBothBoxesAreCheckedListener?.invoke(
                    termsAndConditionsIsChecked && personalDataIsChecked
                )
            }

        with(viewBinding) {
            with(accbTermsAndConditions) {
                supportButtonTintList = ColorStateList.valueOf(palette.mainColor)
                setOnCheckedChangeListener { _, isChecked ->
                    onCheckedChangeListener(isChecked, accbPersonalData.isChecked)
                }
            }

            with(accbPersonalData) {
                supportButtonTintList = ColorStateList.valueOf(palette.mainColor)
                setOnCheckedChangeListener { _, isChecked ->
                    onCheckedChangeListener(isChecked, accbTermsAndConditions.isChecked)
                }
            }
        }
    }

    private fun openLink(isTermsAndConditions: Boolean) {
        val link = if (isTermsAndConditions) {
            TERMS_AND_CONDITIONS_LINK
        } else {
            PERSONAL_DATA_LINK
        }
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(context, intent, bundleOf())
    }

    fun setBothBoxesAreCheckedListener(listener: (Boolean) -> Unit) {
        onBothBoxesAreCheckedListener = listener
    }
}