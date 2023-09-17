package io.dataspike.mobile_sdk.data.use_cases

import io.dataspike.mobile_sdk.data.repository.IDataspikeRepository

internal class ProceedWithVerificationUseCase(
    private val dataspikeRepository: IDataspikeRepository,
) {

    suspend operator fun invoke(shortId: String) =
        dataspikeRepository.proceedWithVerification(shortId, mapOf())
}