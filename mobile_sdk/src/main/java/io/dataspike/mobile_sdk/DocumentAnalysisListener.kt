package io.dataspike.mobile_sdk

import android.graphics.RectF

internal interface DocumentAnalysisListener {

    fun analyseDocument(
        boundingBox: RectF,
    )
}