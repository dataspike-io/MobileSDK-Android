package io.dataspike.mobile_sdk

import android.graphics.RectF

internal interface LivenessAnalysisListener {

    fun analyseLiveness(
        luminosityIsFine: Boolean? = true,
        boundingBox: RectF? = null,
        livenessStatusStringId: Int? = null,
    )
}