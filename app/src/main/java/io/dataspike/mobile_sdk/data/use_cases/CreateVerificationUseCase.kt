package io.dataspike.mobile_sdk.data.use_cases

import io.dataspike.mobile_sdk.data.repository.ISampleAppRepository

internal class CreateVerificationUseCase(
    private val sampleAppRepository: ISampleAppRepository,
) {

    suspend fun invoke(body: Map<String, Array<String>>) =
        sampleAppRepository.createVerification(body)
}