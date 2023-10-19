package io.dataspike.mobile_sdk.domain.models

internal sealed class UploadImageState {

    internal data class UploadImageSuccess(
        val documentId: String,
        val detectedDocumentType: String,
        val detectedDocumentSide: String,
        val detectedTwoSideDocument: Boolean,
        val detectedCountry: String,
        val errors: List<DataspikeErrorDomainModel>,
    ): UploadImageState()

    internal data class UploadImageError(
        val code: Int,
        val message: String,
    ): UploadImageState()
}