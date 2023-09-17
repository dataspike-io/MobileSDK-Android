package io.dataspike.mobile_sdk.domain.models

internal data class ChecksDomainModel(
    val poiIsRequired: Boolean,
    val livenessIsRequired: Boolean,
    val poaIsRequired: Boolean,
)