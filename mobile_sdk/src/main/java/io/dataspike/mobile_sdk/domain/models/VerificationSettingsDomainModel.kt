package io.dataspike.mobile_sdk.domain.models

internal data class VerificationSettingsDomainModel(
    val poiRequired: Boolean,
    val poiAllowedDocuments: List<String>,
    val faceComparisonRequired: Boolean,
    val faceComparisonAllowedDocuments: List<String>,
    val poaRequired: Boolean,
    val poaAllowedDocuments: List<String>,
    val countries: List<String>,
)