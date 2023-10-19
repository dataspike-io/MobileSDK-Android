package io.dataspike.mobile_sdk.data.models.responses

import com.google.gson.annotations.SerializedName

internal data class DataspikeVerificationChecksResponse(
    @SerializedName("face_comparison")
    val faceComparison: DataspikeCheckResponse?,
    @SerializedName("liveness")
    val liveness: DataspikeCheckResponse?,
    @SerializedName("document_mrz")
    val documentMrz: DataspikeCheckResponse?,
    @SerializedName("poa")
    val poa: DataspikeCheckResponse?,
)