package io.dataspike.mobile_sdk.view.custom_views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import io.dataspike.mobile_sdk.R
import io.dataspike.mobile_sdk.databinding.VerificationResultLayoutBinding

internal class VerificationResultLayout @JvmOverloads constructor(
    context: Context,
    attrSet: AttributeSet? = null
): ConstraintLayout(context, attrSet) {

    private val viewBinding = VerificationResultLayoutBinding.inflate(
        LayoutInflater.from(context),
        this
    )

    fun setupError() {
        with(viewBinding) {
            ivCheckmark.setImageDrawable(
                ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.red_x,
                    null
                )
            )
            tvVerificationCompleteTitle.text =
                context.getString(R.string.verification_failed)
            tvVerificationCompleteDescription.text =
                context.getString(R.string.something_went_wrong_this_time)
        }
    }
}