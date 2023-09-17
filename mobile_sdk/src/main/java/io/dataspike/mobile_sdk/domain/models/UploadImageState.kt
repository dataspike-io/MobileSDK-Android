package io.dataspike.mobile_sdk.domain.models

internal sealed class UploadImageState {

    internal data class UploadImageSuccess(
        val documentId: String,
        val detectedDocumentType: String,
        val detectedDocumentSide: String,
        val detectedTwoSideDocument: Boolean,
        val errors: List<ErrorDomainModel>,
    ): UploadImageState()

    internal data class UploadImageError(
        val message: String,
    ): UploadImageState()
}