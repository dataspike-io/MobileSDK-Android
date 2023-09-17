package io.dataspike.mobile_sdk.domain.models

internal sealed class NewVerificationState {

    internal data class NewVerificationSuccess(
        val id: String,
        val status: String,
        val checks: VerificationChecksDomainModel,
        val createdAt: String,
        val isSandbox: Boolean,
        val verificationUrl: String,
        val verificationUrlId: String,
        val expiresAt: String,
    ): NewVerificationState()

    internal data class NewVerificationError(
        val validationError: String,
        val error: String,
    ): NewVerificationState()
}