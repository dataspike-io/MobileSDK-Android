package io.dataspike.mobile_sdk.view.custom_views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import io.dataspike.mobile_sdk.databinding.VerificationResultLayoutBinding
import io.dataspike.mobile_sdk.utils.setup
import io.dataspike.mobile_sdk.view.UIManager

internal class VerificationResultLayout @JvmOverloads constructor(
    context: Context,
    attrSet: AttributeSet? = null
): ConstraintLayout(context, attrSet) {

    private val viewBinding = VerificationResultLayoutBinding.inflate(
        LayoutInflater.from(context),
        this
    )
    private val typography = UIManager.getUiConfig().theme.typography
    private val imageLinks = UIManager.getUiConfig().links.verificationResult
    private val completeMessage = UIManager.getUiConfig().messages.verificationSuccessful

    init {
        with(viewBinding) {
            Glide
                .with(ivCheckmark)
                .load(imageLinks.verificationSuccessful)
                .into(ivCheckmark)
            tvVerificationCompleteTitle.setup(
                font = typography.bodyOne.font,
                textSize = typography.bodyOne.textSize,
                textColor = typography.bodyOne.textColor,
            )
            with(tvVerificationCompleteDescription) {
                setup(
                    font = typography.bodyTwo.font,
                    textSize = typography.bodyTwo.textSize,
                    textColor = typography.bodyTwo.textColor,
                )
                text = completeMessage
            }
        }
    }
}