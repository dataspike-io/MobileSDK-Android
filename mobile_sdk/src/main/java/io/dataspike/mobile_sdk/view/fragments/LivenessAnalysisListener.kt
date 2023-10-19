package io.dataspike.mobile_sdk.view.fragments

import android.graphics.RectF

internal interface LivenessAnalysisListener {

    fun analyzeLiveness(
        luminosityIsFine: Boolean? = true,
        boundingBox: RectF? = null,
        livenessStatusStringId: Int? = null,
    )
}