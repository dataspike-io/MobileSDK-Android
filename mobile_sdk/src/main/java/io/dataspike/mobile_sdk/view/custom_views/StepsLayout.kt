package io.dataspike.mobile_sdk.view.custom_views

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import io.dataspike.mobile_sdk.databinding.StepsLayoutBinding
import io.dataspike.mobile_sdk.dependencies_provider.DataspikeInjector
import io.dataspike.mobile_sdk.utils.dpToPx
import io.dataspike.mobile_sdk.utils.getFont
import io.dataspike.mobile_sdk.view.POA
import io.dataspike.mobile_sdk.view.POI_BACK
import io.dataspike.mobile_sdk.view.POI_FRONT
import io.dataspike.mobile_sdk.view.UIManager

internal class StepsLayout @JvmOverloads constructor(
    context: Context,
    attrSet: AttributeSet? = null
): ConstraintLayout(context, attrSet) {

    private val viewBinding = StepsLayoutBinding.inflate(
        LayoutInflater.from(context),
        this
    )
    private val palette = UIManager.getUiConfig().theme.palette
    private val typography = UIManager.getUiConfig().theme.typography
    private val showSteps = UIManager.getUiConfig().options.showSteps

    fun setup(
        step: String?,
        stepIsSuccessful: Boolean = false,
    ) {
        val checks = DataspikeInjector.component.verificationManager.checks

        with(viewBinding) {
            if (showSteps) {
                val mainColor = palette.mainColor
                val successColor = palette.successColor
                val bodyTwoTextColor = typography.bodyTwo.textColor
                val currentStepDrawable = GradientDrawable().apply {
                    shape = GradientDrawable.RECTANGLE
                    setSize(dpToPx(100F).toInt(), dpToPx(6F).toInt())
                    color = ColorStateList.valueOf(mainColor)
                    cornerRadius = dpToPx(100F)
                }
                val successfulStepDrawable = GradientDrawable().apply {
                    shape = GradientDrawable.RECTANGLE
                    setSize(dpToPx(100F).toInt(), dpToPx(6F).toInt())
                    color = ColorStateList.valueOf(successColor)
                    cornerRadius = dpToPx(100F)
                }
                val nextStepDrawable = GradientDrawable().apply {
                    shape = GradientDrawable.RECTANGLE
                    setSize(dpToPx(100F).toInt(), dpToPx(6F).toInt())
                    color = ColorStateList.valueOf(bodyTwoTextColor)
                    cornerRadius = dpToPx(100F)
                }

                llPoi.isVisible = checks.poiIsRequired
                llLiveness.isVisible = checks.livenessIsRequired
                llPoa.isVisible = checks.poaIsRequired
                tvPoi.textSize = typography.bodyOne.textSize
                tvPoi.typeface = getFont(context, typography.bodyOne.font)
                tvLiveness.textSize = typography.bodyOne.textSize
                tvLiveness.typeface = getFont(context, typography.bodyOne.font)
                tvPoa.textSize = typography.bodyOne.textSize
                tvPoa.typeface = getFont(context, typography.bodyOne.font)

                when (step) {
                    POI_FRONT, POI_BACK -> {
                        if (stepIsSuccessful) {
                            tvPoi.setTextColor(successColor)
                            ivPoi.setImageDrawable(successfulStepDrawable)
                        } else {
                            tvPoi.setTextColor(mainColor)
                            ivPoi.setImageDrawable(currentStepDrawable)
                        }

                        tvLiveness.setTextColor(bodyTwoTextColor)
                        ivLiveness.setImageDrawable(nextStepDrawable)
                        tvPoa.setTextColor(bodyTwoTextColor)
                        ivPoa.setImageDrawable(nextStepDrawable)
                    }

                    POA -> {
                        if (stepIsSuccessful) {
                            tvPoa.setTextColor(successColor)
                            ivPoa.setImageDrawable(successfulStepDrawable)
                        } else {
                            tvPoa.setTextColor(mainColor)
                            ivPoa.setImageDrawable(currentStepDrawable)
                        }

                        tvPoi.setTextColor(successColor)
                        ivPoi.setImageDrawable(successfulStepDrawable)
                        tvLiveness.setTextColor(successColor)
                        ivLiveness.setImageDrawable(successfulStepDrawable)
                    }
                }
            } else {
                visibility = View.GONE
            }
        }
    }
}