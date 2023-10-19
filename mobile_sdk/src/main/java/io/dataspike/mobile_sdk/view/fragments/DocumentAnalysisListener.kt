package io.dataspike.mobile_sdk.view.fragments

import android.graphics.RectF

internal interface DocumentAnalysisListener {

    fun analyzeDocument(
        boundingBox: RectF,
    )
}