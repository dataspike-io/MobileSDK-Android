package io.dataspike.mobile_sdk.view.ui_models

internal sealed class UploadImageUiState {

    internal data class UploadImageUiSuccess(
        val shouldNavigateToSelectCountryFragment: Boolean,
        val fragmentToNavigateToOnContinue: String,
    ): UploadImageUiState()

    internal data class UploadImageUiError(
        val shouldNavigateToVerificationExpiredFragment: Boolean,
        val errorMessage: String,
    ): UploadImageUiState()
}