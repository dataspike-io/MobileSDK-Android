package io.dataspike.mobile_sdk.data.models.responses

import com.google.gson.annotations.SerializedName

internal data class ProceedWithVerificationErrorResponse(
    @SerializedName("error")
    val error: String?,
    @SerializedName("pending_documents")
    val pendingDocuments: List<String>?,
    @SerializedName("message")
    val message: String?,
)