package io.dataspike.mobile_sdk.view.models

internal sealed class NewVerificationUiState {

    internal data class NewVerificationUiSuccess(
        val shortId: String,
    ): NewVerificationUiState()

    internal data class NewVerificationUiError(
        val error: String,
        val details: String,
    ): NewVerificationUiState()
}