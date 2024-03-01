package io.dataspike.mobile_sdk.view.ui_models

internal data class VerificationResultLinkModel(
    var verificationSuccessful: String,
    var verificationExpired: String,
    var verificationFailed: String,
) {

    companion object {
        val defaultOnboardingLinks = VerificationResultLinkModel(
            verificationSuccessful =
            "https://static.dataspike.io/images/sdk/verification/success-icon.png",
            verificationExpired =
            "https://static.dataspike.io/images/sdk/verification/time-expired.png",
            verificationFailed =
            "https://static.dataspike.io/images/sdk/verification/error-icon.png",
        )
    }
}