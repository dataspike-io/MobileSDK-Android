package io.dataspike.mobile_sdk.data.models.responses

import com.google.gson.annotations.SerializedName

internal data class DataspikeCheckResponse(
    @SerializedName("status")
    val status: String?,
    @SerializedName("errors")
    val errors: List<DataspikeErrorResponse>?,
    @SerializedName("pending_documents")
    val pendingDocuments: List<String>?,
)