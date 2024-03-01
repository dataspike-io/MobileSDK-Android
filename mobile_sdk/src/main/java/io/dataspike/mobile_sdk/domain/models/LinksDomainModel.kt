package io.dataspike.mobile_sdk.domain.models

import com.google.gson.annotations.SerializedName

internal data class LinksDomainModel(
    @SerializedName("onboarding")
    var onboarding: OnboardingLinksDomainModel? = null,
    @SerializedName("verification_result")
    var verificationResult : VerificationResultLinkDomainModel? = null,
    @SerializedName("intro")
    var intro: IntroLinkDomainModel? = null,
    @SerializedName("requirements")
    var requirements: RequirementsLinkDomainModel? = null,
)