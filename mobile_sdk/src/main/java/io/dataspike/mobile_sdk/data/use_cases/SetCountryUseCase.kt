package io.dataspike.mobile_sdk.data.use_cases

import io.dataspike.mobile_sdk.data.models.requests.CountryRequestBody
import io.dataspike.mobile_sdk.data.repository.IDataspikeRepository
import io.dataspike.mobile_sdk.domain.models.EmptyState

internal class SetCountryUseCase(
    private val dataspikeRepository: IDataspikeRepository,
) {

    suspend operator fun invoke(body: CountryRequestBody): EmptyState {
        return dataspikeRepository.setCountry(body)
    }
}