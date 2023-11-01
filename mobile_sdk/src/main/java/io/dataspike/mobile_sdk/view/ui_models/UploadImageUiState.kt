package io.dataspike.mobile_sdk.view.ui_models

internal sealed class UploadImageUiState {

    internal data class UploadImageUiSuccess(
        val shouldNavigateToSelectCountryFragment: Boolean,
        val fragmentToNavigateToOnContinue: String,
    ): UploadImageUiState()

    internal data class UploadImageUiError(
        val isExpired: Boolean,
        val tooManyAttempts: Boolean,
        val errorMessage: String,
        val fragmentToNavigateTo: String,
    ): UploadImageUiState()
}