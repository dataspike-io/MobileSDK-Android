package io.dataspike.mobile_sdk.view.ui_models

import io.dataspike.mobile_sdk.DataspikeVerificationStatus

internal sealed class ProceedWithVerificationUiState {
    internal data class ProceedWithVerificationUiSuccess(
        val verificationStatus: DataspikeVerificationStatus,
    ) : ProceedWithVerificationUiState()

    internal object ProceedWithVerificationUiError : ProceedWithVerificationUiState()
}