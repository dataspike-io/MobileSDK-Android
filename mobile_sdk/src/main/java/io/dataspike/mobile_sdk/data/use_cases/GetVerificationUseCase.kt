package io.dataspike.mobile_sdk.data.use_cases

import io.dataspike.mobile_sdk.data.repository.IDataspikeRepository
import io.dataspike.mobile_sdk.domain.VerificationChecksManager
import io.dataspike.mobile_sdk.domain.models.VerificationState

internal class GetVerificationUseCase(
    private val dataspikeRepository: IDataspikeRepository,
    private val verificationSettingsManager: VerificationChecksManager,
) {

    suspend operator fun invoke(shortId: String): VerificationState {
        val state = dataspikeRepository.getVerification(shortId)

        if (state is VerificationState.VerificationSuccess) {
            verificationSettingsManager.setChecks(state.settings)
        }

        return state
    }
}