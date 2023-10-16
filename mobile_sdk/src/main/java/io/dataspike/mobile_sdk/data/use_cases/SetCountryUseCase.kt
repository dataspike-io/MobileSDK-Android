package io.dataspike.mobile_sdk.data.use_cases

import io.dataspike.mobile_sdk.data.repository.IDataspikeRepository

internal class SetCountryUseCase(
    private val dataspikeRepository: IDataspikeRepository,
) {

    suspend operator fun invoke(shortId: String, body: Map<String, String>) =
        dataspikeRepository.setCountry(shortId, body)
}