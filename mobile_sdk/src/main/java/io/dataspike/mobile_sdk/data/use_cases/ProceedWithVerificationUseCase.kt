package io.dataspike.mobile_sdk.data.use_cases

import io.dataspike.mobile_sdk.data.repository.IDataspikeRepository
import io.dataspike.mobile_sdk.domain.models.ProceedWithVerificationState

internal class ProceedWithVerificationUseCase(
    private val dataspikeRepository: IDataspikeRepository,
) {

    suspend operator fun invoke(): ProceedWithVerificationState {
        return dataspikeRepository.proceedWithVerification()
    }
}