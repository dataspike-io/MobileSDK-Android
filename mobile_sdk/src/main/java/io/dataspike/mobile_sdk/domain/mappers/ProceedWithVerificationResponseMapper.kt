package io.dataspike.mobile_sdk.domain.mappers

import io.dataspike.mobile_sdk.data.models.responses.ProceedWithVerificationErrorResponse
import io.dataspike.mobile_sdk.data.models.responses.ProceedWithVerificationResponse
import io.dataspike.mobile_sdk.domain.models.ProceedWithVerificationState
import io.dataspike.mobile_sdk.utils.deserializeFromJson
import retrofit2.HttpException

internal class ProceedWithVerificationResponseMapper {

    private fun HttpException.toProceedWithVerificationErrorMessage()
            : ProceedWithVerificationState {
        val errorResponse =
            response()
                ?.errorBody()
                ?.string()
                .deserializeFromJson(ProceedWithVerificationErrorResponse::class.java)

        return ProceedWithVerificationState.ProceedWithVerificationStateError(
            error = errorResponse?.error ?: "",
            pendingDocuments = errorResponse?.pendingDocuments ?: emptyList(),
            message = errorResponse?.message ?: "",
        )
    }

    fun map(result: Result<ProceedWithVerificationResponse>): ProceedWithVerificationState {
        result
            .onSuccess { proceedWithVerificationResponse ->
                return ProceedWithVerificationState.ProceedWithVerificationStateSuccess(
                    id = proceedWithVerificationResponse.id ?: "",
                    status = proceedWithVerificationResponse.status ?: "",
                )
            }
            .onFailure { throwable ->
                return if (throwable is HttpException) {
                    throwable.toProceedWithVerificationErrorMessage()
                } else {
                    ProceedWithVerificationState.ProceedWithVerificationStateError(
                        error = "",
                        pendingDocuments = emptyList(),
                        message = "",
                    )
                }
            }

        throw IllegalStateException("Unknown error occurred")
    }
}