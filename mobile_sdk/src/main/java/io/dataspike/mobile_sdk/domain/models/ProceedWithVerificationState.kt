package io.dataspike.mobile_sdk.domain.models

internal sealed class ProceedWithVerificationState {

    internal data class ProceedWithVerificationStateSuccess(
        val id: String,
        val status: String,
    ): ProceedWithVerificationState()

    internal data class ProceedWithVerificationStateError(
        val error: String,
        val pendingDocuments: List<String>,
        val message: String,
    ): ProceedWithVerificationState()
}