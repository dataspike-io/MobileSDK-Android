package io.dataspike.mobile_sdk.domain.mappers

import com.google.gson.Gson
import io.dataspike.mobile_sdk.data.models.responses.DataspikeHttpErrorResponse
import io.dataspike.mobile_sdk.data.models.responses.ProceedWithVerificationResponse
import io.dataspike.mobile_sdk.domain.models.ProceedWithVerificationState
import retrofit2.HttpException

internal class ProceedWithVerificationResponseMapper {

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
                        error = "Unknown error occurred",
                        details = "Try again later",
                    )
                }
            }

        throw IllegalStateException("Unknown error occurred")
    }

    private fun HttpException.toProceedWithVerificationErrorMessage()
            : ProceedWithVerificationState {
        val errorResponse = kotlin.runCatching {
            Gson().fromJson(
                response()?.errorBody()?.string(),
                DataspikeHttpErrorResponse::class.java
            )
        }.onFailure { throwable -> throwable.printStackTrace() }.getOrNull()

        return ProceedWithVerificationState.ProceedWithVerificationStateError(
            error = errorResponse?.error ?: "Unknown error occurred",
            details = errorResponse?.details ?: "Try again later",
        )
    }
}