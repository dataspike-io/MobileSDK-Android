package io.dataspike.mobile_sdk.domain.models

internal data class CountryDomainModel(
    val alphaTwo: String,
    val alphaThree: String,
    val name: String,
    val continent: String,
    var isSelected: Boolean = false,
)