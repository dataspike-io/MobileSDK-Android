package io.dataspike.mobile_sdk.data.use_cases

import io.dataspike.mobile_sdk.data.repository.IDataspikeRepository
import io.dataspike.mobile_sdk.domain.VerificationManager
import io.dataspike.mobile_sdk.domain.models.VerificationState
import io.dataspike.mobile_sdk.view.UIManager

internal class GetVerificationUseCase(
    private val dataspikeRepository: IDataspikeRepository,
    private val verificationSettingsManager: VerificationManager,
) {

    suspend operator fun invoke(darkModeIsEnabled: Boolean): VerificationState {
        val state = dataspikeRepository.getVerification(darkModeIsEnabled)

        if (state is VerificationState.VerificationSuccess) {
            UIManager.initUIManager(state.settings.uiConfig)
            verificationSettingsManager.setChecksAndExpiration(
                settings = state.settings,
                status = state.status,
                expiresAt = state.expiresAt,
            )
        }

        return state
    }
}