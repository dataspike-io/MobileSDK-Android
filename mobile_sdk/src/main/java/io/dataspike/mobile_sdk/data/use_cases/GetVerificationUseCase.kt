package io.dataspike.mobile_sdk.data.use_cases

import io.dataspike.mobile_sdk.data.repository.IDataspikeRepository
import io.dataspike.mobile_sdk.domain.VerificationManager
import io.dataspike.mobile_sdk.domain.models.VerificationState

internal class GetVerificationUseCase(
    private val dataspikeRepository: IDataspikeRepository,
    private val verificationSettingsManager: VerificationManager,
) {

    suspend operator fun invoke(): VerificationState {
        val state = dataspikeRepository.getVerification()

        if (state is VerificationState.VerificationSuccess) {
            verificationSettingsManager.setChecksAndExpiration(
                settings = state.settings,
                status = state.status,
                expiresAt = state.expiresAt
            )
        }

        return state
    }
}