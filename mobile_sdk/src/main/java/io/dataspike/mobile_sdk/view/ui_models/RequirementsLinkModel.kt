package io.dataspike.mobile_sdk.view.ui_models

internal data class RequirementsLinkModel(
    var poi: String,
    var liveness: String,
    var poa: String,
) {

    companion object {
        val defaultOnboardingLinks = RequirementsLinkModel(
            poi = "https://dash.dataspike.io/widget/requirements/docs/",
            liveness = "https://dash.dataspike.io/widget/requirements/selfie/",
            poa = "https://dash.dataspike.io/widget/requirements/poa/",
        )
    }
}