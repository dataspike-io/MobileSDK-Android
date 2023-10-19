package io.dataspike.mobile_sdk.view.custom_views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import io.dataspike.mobile_sdk.databinding.RequirementsLayoutBinding
import io.dataspike.mobile_sdk.view.LIVENESS
import io.dataspike.mobile_sdk.view.POA
import io.dataspike.mobile_sdk.view.POI_BACK
import io.dataspike.mobile_sdk.view.POI_FRONT
internal class RequirementsLayout @JvmOverloads constructor(
    context: Context,
    attrSet: AttributeSet? = null
): ConstraintLayout(context, attrSet) {

    private val viewBinding = RequirementsLayoutBinding.inflate(
        LayoutInflater.from(context),
        this
    )

    fun setup(imageType: String?) {
        with(viewBinding) {
            when (imageType) {
                POI_FRONT, POI_BACK -> {
                    clPoiRequirements.root.visibility = VISIBLE
                }

                LIVENESS -> {
                    clLivenessRequirements.root.visibility = VISIBLE
                }

                POA -> {
                    clPoaRequirements.root.visibility = VISIBLE
                }
            }
        }
    }
}