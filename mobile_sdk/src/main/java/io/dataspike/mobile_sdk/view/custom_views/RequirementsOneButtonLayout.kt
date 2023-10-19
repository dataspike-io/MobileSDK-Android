package io.dataspike.mobile_sdk.view.custom_views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout
import io.dataspike.mobile_sdk.databinding.RequirementsOneButtonLayoutBinding

internal class RequirementsOneButtonLayout @JvmOverloads constructor(
    context: Context,
    attrSet: AttributeSet? = null
): ConstraintLayout(context, attrSet) {

    private val viewBinding = RequirementsOneButtonLayoutBinding.inflate(
        LayoutInflater.from(context),
        this
    )

    fun setup(
        @StringRes stringResId: Int,
        continueButtonAction: () -> Unit,
        openRequirementsAction: () -> Unit,
    ) {
        with(viewBinding) {
            with(mbContinue) {
                text = context.getString(stringResId)
                setOnClickListener { continueButtonAction.invoke() }
            }
            tvRequirements.setOnClickListener {
                openRequirementsAction.invoke()
            }
        }
    }
}