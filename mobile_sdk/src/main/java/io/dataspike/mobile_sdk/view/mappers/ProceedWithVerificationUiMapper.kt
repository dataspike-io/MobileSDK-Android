package io.dataspike.mobile_sdk.view.mappers

import io.dataspike.mobile_sdk.DataspikeVerificationStatus
import io.dataspike.mobile_sdk.domain.models.ProceedWithVerificationState
import io.dataspike.mobile_sdk.view.ui_models.ProceedWithVerificationUiState
import io.dataspike.mobile_sdk.view.view_models.COMPLETED
import io.dataspike.mobile_sdk.view.view_models.EXPIRED

internal class ProceedWithVerificationUiMapper {

    fun map(proceedWithVerificationState: ProceedWithVerificationState)
    : ProceedWithVerificationUiState {
        when (proceedWithVerificationState) {
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

                return ProceedWithVerificationUiState.ProceedWithVerificationUiSuccess(
                    verificationStatus = verificationStatus,
                )
            }
            is ProceedWithVerificationState.ProceedWithVerificationStateError -> {
                return ProceedWithVerificationUiState.ProceedWithVerificationUiError
            }
        }
    }
}