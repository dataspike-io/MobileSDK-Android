package io.dataspike.mobile_sdk.view.mappers

import io.dataspike.mobile_sdk.DataspikeVerificationStatus
import io.dataspike.mobile_sdk.domain.models.ProceedWithVerificationState
import io.dataspike.mobile_sdk.view.ui_models.ProceedWithVerificationUiState
import io.dataspike.mobile_sdk.view.view_models.COMPLETED
import io.dataspike.mobile_sdk.view.view_models.EXPIRED

internal class ProceedWithVerificationUiMapper {

    fun map(proceedWithVerificationState: ProceedWithVerificationState)
    : ProceedWithVerificationUiState {
        return when (proceedWithVerificationState) {
            is ProceedWithVerificationState.ProceedWithVerificationStateSuccess -> {
                val verificationStatus = when (proceedWithVerificationState.status) {
                    COMPLETED -> {
                        DataspikeVerificationStatus.VERIFICATION_SUCCESSFUL
                    }

                    EXPIRED -> {
                        DataspikeVerificationStatus.VERIFICATION_EXPIRED
                    }

                    else -> {
                        DataspikeVerificationStatus.VERIFICATION_FAILED
                    }
                }

                ProceedWithVerificationUiState.ProceedWithVerificationUiSuccess(
                    verificationStatus = verificationStatus,
                )
            }
            is ProceedWithVerificationState.ProceedWithVerificationStateError -> {
                ProceedWithVerificationUiState.ProceedWithVerificationUiError
            }
        }
    }
}