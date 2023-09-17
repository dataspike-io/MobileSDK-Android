package io.dataspike.mobile_sdk.data.models.responses

import com.google.gson.annotations.SerializedName

internal data class VerificationChecksResponse(
    @SerializedName("face_comparison")
    val faceComparison: CheckResponse?,
    @SerializedName("liveness")
    val liveness: CheckResponse?,
    @SerializedName("document_mrz")
    val documentMrz: CheckResponse?,
    @SerializedName("poa")
    val poa: CheckResponse?,
)