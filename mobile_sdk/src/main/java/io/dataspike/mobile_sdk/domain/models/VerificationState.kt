package io.dataspike.mobile_sdk.domain.models

internal sealed class VerificationState {

    internal data class VerificationSuccess(
        val id: String,
        val status: String,
        val checks: VerificationChecksDomainModel,
        val redirectUrl: String,
        val countryCode: String,
        val settings: VerificationSettingsDomainModel,
    ): VerificationState()

    internal data class VerificationError(
        val details: String,
        val error: String,
    ): VerificationState()
}