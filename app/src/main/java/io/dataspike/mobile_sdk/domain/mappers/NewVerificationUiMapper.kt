package io.dataspike.mobile_sdk.domain.mappers

import io.dataspike.mobile_sdk.domain.models.NewVerificationState
import io.dataspike.mobile_sdk.view.models.NewVerificationUiState

internal class NewVerificationUiMapper {

    fun map(newVerificationState: NewVerificationState): NewVerificationUiState {
        return when (newVerificationState) {
            is NewVerificationState.NewVerificationSuccess -> {
                NewVerificationUiState.NewVerificationUiSuccess(
                    shortId = newVerificationState.verificationUrlId,
                )
            }

            is NewVerificationState.NewVerificationError -> {
                NewVerificationUiState.NewVerificationUiError(
                    error = newVerificationState.error,
                    details = newVerificationState.validationError,
                )
            }
        }
    }
}