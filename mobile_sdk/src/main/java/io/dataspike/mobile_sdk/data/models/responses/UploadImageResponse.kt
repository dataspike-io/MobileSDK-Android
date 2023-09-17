package io.dataspike.mobile_sdk.data.models.responses

import com.google.gson.annotations.SerializedName

internal data class UploadImageResponse(
    @SerializedName("document_id")
    val documentId: String?,
    @SerializedName("detected_document_type")
    val detectedDocumentType: String?,
    @SerializedName("detected_document_side")
    val detectedDocumentSide: String?,
    @SerializedName("detected_two_side_document")
    val detectedTwoSideDocument: Boolean?,
    @SerializedName("errors")
    val errors: List<ErrorResponse>?,
)