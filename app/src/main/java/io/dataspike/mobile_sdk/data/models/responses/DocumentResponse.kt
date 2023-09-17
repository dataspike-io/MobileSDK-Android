package io.dataspike.mobile_sdk.data.models.responses

import com.google.gson.annotations.SerializedName

data class DocumentResponse(
    @SerializedName("document_id")
    val documentId: String?,
    @SerializedName("document_type")
    val documentType: String?,
)