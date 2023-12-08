package io.dataspike.mobile_sdk.view.custom_views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import io.dataspike.mobile_sdk.R
import io.dataspike.mobile_sdk.databinding.StepsLayoutBinding
import io.dataspike.mobile_sdk.dependencies_provider.DataspikeInjector
import io.dataspike.mobile_sdk.view.LIVENESS
import io.dataspike.mobile_sdk.view.POA
import io.dataspike.mobile_sdk.view.POI_BACK
import io.dataspike.mobile_sdk.view.POI_FRONT

internal class StepsLayout @JvmOverloads constructor(
    context: Context,
    attrSet: AttributeSet? = null
): ConstraintLayout(context, attrSet) {

    private val viewBinding = StepsLayoutBinding.inflate(
        LayoutInflater.from(context),
        this
    )

    fun setup(
        step: String?,
        stepIsSuccessful: Boolean = false,
        ) {
        val checks = DataspikeInjector.component.verificationManager.checks

        with(viewBinding) {
            val lightGreenColor = ContextCompat.getColor(context, R.color.light_green)
            val lighterGreyColor = ContextCompat.getColor(context, R.color.lighter_grey)
            val dsPurpleColor = ContextCompat.getColor(context, R.color.ds_purple)

            llPoi.isVisible = checks.poiIsRequired
            llLiveness.isVisible = checks.livenessIsRequired
            llPoa.isVisible = checks.poaIsRequired

            when (step) {
                POI_FRONT, POI_BACK -> {
                    if (stepIsSuccessful) {
                        tvPoi.setTextColor(lightGreenColor)
                        ivPoi.setColorFilter(lightGreenColor)
                    } else {
                        tvPoi.setTextColor(dsPurpleColor)
                        ivPoi.setColorFilter(dsPurpleColor)
                    }

                    tvLiveness.setTextColor(lighterGreyColor)
                    ivLiveness.setColorFilter(lighterGreyColor)
                    tvPoa.setTextColor(lighterGreyColor)
                    ivPoa.setColorFilter(lighterGreyColor)
                }

                LIVENESS -> {
                    if (stepIsSuccessful) {
                        tvLiveness.setTextColor(lightGreenColor)
                        ivLiveness.setColorFilter(lightGreenColor)
                    } else {
                        tvLiveness.setTextColor(dsPurpleColor)
                        ivLiveness.setColorFilter(dsPurpleColor)
                    }

                    tvPoi.setTextColor(lightGreenColor)
                    ivPoi.setColorFilter(lightGreenColor)
                    tvPoa.setTextColor(lighterGreyColor)
                    ivPoa.setColorFilter(lighterGreyColor)
                }

                POA -> {
                    if (stepIsSuccessful) {
                        tvPoa.setTextColor(lightGreenColor)
                        ivPoa.setColorFilter(lightGreenColor)
                    } else {
                        tvPoa.setTextColor(dsPurpleColor)
                        ivPoa.setColorFilter(dsPurpleColor)
                    }

                    tvPoi.setTextColor(lightGreenColor)
                    ivPoi.setColorFilter(lightGreenColor)
                    tvLiveness.setTextColor(lightGreenColor)
                    ivLiveness.setColorFilter(lightGreenColor)
                }
            }
        }
    }
}