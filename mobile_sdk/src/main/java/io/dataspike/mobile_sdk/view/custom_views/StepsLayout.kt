package io.dataspike.mobile_sdk.view.custom_views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import io.dataspike.mobile_sdk.R
import io.dataspike.mobile_sdk.databinding.StepsLayoutBinding
import io.dataspike.mobile_sdk.domain.VerificationManager
import io.dataspike.mobile_sdk.utils.Utils.visible
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

    fun setStepsState(
        step: String,
        stepIsSuccessful: Boolean = false,
        ) {
        val checks = VerificationManager.checks

        with(viewBinding) {
            val lightGreenColor = ResourcesCompat.getColor(
                resources,
                R.color.light_green,
                null
            )
            val lighterGreyColor = ResourcesCompat.getColor(
                resources,
                R.color.lighter_grey,
                null
            )
            val dsPurpleColor = ResourcesCompat.getColor(
                resources,
                R.color.ds_purple,
                null
            )

            llPoi.visible(checks.poiIsRequired)
            llLiveness.visible(checks.livenessIsRequired)
            llPoa.visible(checks.poaIsRequired)

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