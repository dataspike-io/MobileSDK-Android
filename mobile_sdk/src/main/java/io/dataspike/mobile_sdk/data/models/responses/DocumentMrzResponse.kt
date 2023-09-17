package io.dataspike.mobile_sdk.data.models.responses

import com.google.gson.annotations.SerializedName

internal data class DocumentMrzResponse(
    @SerializedName("data")
    val data: DocumentMrzDataResponse?,
    @SerializedName("status")
    val status: String?,
    @SerializedName("pending_documents")
    val pendingDocuments: List<String>?,
    @SerializedName("errors")
    val errors: List<ErrorResponse>?,
)