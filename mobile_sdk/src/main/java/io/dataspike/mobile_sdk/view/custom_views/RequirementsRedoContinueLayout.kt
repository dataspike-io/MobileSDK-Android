package io.dataspike.mobile_sdk.view.custom_views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import io.dataspike.mobile_sdk.R
import io.dataspike.mobile_sdk.databinding.RequirementsRedoContinueLayoutBinding

internal class RequirementsRedoContinueLayout @JvmOverloads constructor(
    context: Context,
    attrSet: AttributeSet? = null
): ConstraintLayout(context, attrSet) {

    private val viewBinding = RequirementsRedoContinueLayoutBinding.inflate(
        LayoutInflater.from(context),
        this
    )

    fun setup(
        popBackStackAction: () -> Unit,
        openRequirementsScreenAction: (() -> Unit)? = null,
        continueButtonAction: (() -> Unit)? = null,
        uploadSuccessful: Boolean? = true,
    ) {
        with(viewBinding) {
            mbRedo.setOnClickListener { popBackStackAction.invoke() }

            if (uploadSuccessful == true) {
                tvRequirements.setOnClickListener {
                    openRequirementsScreenAction?.invoke()
                }

                mbContinue.setOnClickListener {
                    continueButtonAction?.invoke()
                }
            } else {
                tvRequirements.visibility = GONE
                mbContinue.visibility = GONE

                with(mbRedo) {
                    background = ResourcesCompat.getDrawable(
                        resources,
                        R.drawable.purple_button_background,
                        null
                    )

                    setTextColor(
                        ResourcesCompat.getColor(
                            resources,
                            R.color.white,
                            null
                        )
                    )
                }
            }
        }
    }
}