package io.dataspike.mobile_sdk.data.use_cases

import io.dataspike.mobile_sdk.data.repository.IDataspikeRepository
import io.dataspike.mobile_sdk.domain.models.CountriesState

internal class GetCountriesUseCase(
    private val dataspikeRepository: IDataspikeRepository,
) {

    suspend operator fun invoke(): CountriesState {
        return dataspikeRepository.getCountries()
    }
}