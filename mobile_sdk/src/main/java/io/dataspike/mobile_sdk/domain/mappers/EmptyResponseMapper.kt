package io.dataspike.mobile_sdk.domain.mappers

import io.dataspike.mobile_sdk.data.models.responses.DataspikeEmptyResponse
import io.dataspike.mobile_sdk.data.models.responses.DataspikeHttpErrorResponse
import io.dataspike.mobile_sdk.domain.models.EmptyState
import io.dataspike.mobile_sdk.utils.deserializeFromJson
import retrofit2.HttpException

internal class EmptyResponseMapper {

    private fun HttpException.toEmptyResponseErrorMessage(): EmptyState {
        val errorResponse =
            response()
                ?.errorBody()
                ?.string()
                .deserializeFromJson(DataspikeHttpErrorResponse::class.java)

        return EmptyState.EmptyStateError(
            error = errorResponse?.error ?: "Unknown error occurred",
            details = errorResponse?.details ?: "Try again later",
        )
    }

    fun map(result: Result<DataspikeEmptyResponse>): EmptyState {
        result
            .onSuccess {
                return EmptyState.EmptyStateSuccess
            }
            .onFailure { throwable ->
                return if (throwable is HttpException) {
                    throwable.toEmptyResponseErrorMessage()
                } else {
                    EmptyState.EmptyStateError(
                        error = "Unknown error occurred",
                        details = "Try again later",
                    )
                }
            }

        throw IllegalStateException("Unknown error occurred")
    }
}