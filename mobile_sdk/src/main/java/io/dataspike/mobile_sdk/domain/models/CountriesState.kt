package io.dataspike.mobile_sdk.domain.models

internal sealed class CountriesState {

    internal data class CountriesSuccess(
        val countries: List<CountryDomainModel>,
    ): CountriesState()

    internal data class CountriesError(
        val message: String,
    ): CountriesState()
}