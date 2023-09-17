package io.dataspike.mobile_sdk

import android.graphics.RectF

internal interface ImageAnalysisListener {

    fun analyseImage(
        luminosityIsFine: Boolean,
        boundingBox: RectF? = null,
        headIsStraight: Boolean? = true,
    )
}