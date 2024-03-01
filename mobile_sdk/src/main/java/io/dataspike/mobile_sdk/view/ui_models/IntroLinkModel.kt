package io.dataspike.mobile_sdk.view.ui_models

internal data class IntroLinkModel(
    var poi: String,
    var poa: String,
) {

    companion object {
        val defaultOnboardingLinks = IntroLinkModel(
            poi = "https://static.dataspike.io/images/sdk/verification/main-page.png",
            poa = "https://static.dataspike.io/images/sdk/verification/poa-document.png",
        )
    }
}