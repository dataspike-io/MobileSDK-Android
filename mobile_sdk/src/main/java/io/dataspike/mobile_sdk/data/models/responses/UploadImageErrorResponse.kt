package io.dataspike.mobile_sdk.data.models.responses

import com.google.gson.annotations.SerializedName

internal data class UploadImageErrorResponse(
    @SerializedName("document_id")
    val documentId: String?,
    @SerializedName("errors")
    val errors: List<DataspikeErrorResponse>?,
)