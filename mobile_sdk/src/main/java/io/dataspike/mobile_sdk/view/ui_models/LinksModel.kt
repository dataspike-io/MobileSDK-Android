package io.dataspike.mobile_sdk.view.ui_models

internal data class LinksModel(
    var onboarding: OnboardingLinksModel,
    var verificationResult: VerificationResultLinkModel,
    var intro: IntroLinkModel,
    var requirements: RequirementsLinkModel,
) {

    companion object {
        val defaultLinks = LinksModel(
            onboarding = OnboardingLinksModel.defaultOnboardingLinks,
            verificationResult = VerificationResultLinkModel.defaultOnboardingLinks,
            intro = IntroLinkModel.defaultOnboardingLinks,
            requirements = RequirementsLinkModel.defaultOnboardingLinks,
        )
    }
}