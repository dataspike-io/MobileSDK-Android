package io.dataspike.mobile_sdk.data.use_cases

import io.dataspike.mobile_sdk.data.repository.IDataspikeRepository

internal class GetCountriesUseCase(
    private val dataspikeRepository: IDataspikeRepository,
) {

    suspend operator fun invoke() =
        dataspikeRepository.getCountries()
}