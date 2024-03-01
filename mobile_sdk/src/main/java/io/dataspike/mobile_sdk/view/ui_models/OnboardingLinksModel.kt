package io.dataspike.mobile_sdk.view.ui_models

internal data class OnboardingLinksModel(
    var poi: String,
    var liveness: String,
    var poa: String,
) {

    companion object {
        val defaultOnboardingLinks = OnboardingLinksModel(
            poi = "https://static.dataspike.io/images/sdk/verification/document.png",
            liveness = "https://static.dataspike.io/images/sdk/verification/selfie.png",
            poa = "https://static.dataspike.io/images/sdk/verification/poa.png",
        )
    }
}