package io.dataspike.mobile_sdk.domain.mappers

import com.google.gson.Gson
import io.dataspike.mobile_sdk.data.models.responses.DataspikeEmptyResponse
import io.dataspike.mobile_sdk.data.models.responses.DataspikeHttpErrorResponse
import io.dataspike.mobile_sdk.domain.models.EmptyState
import retrofit2.HttpException

internal class EmptyResponseMapper {

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

    private fun HttpException.toEmptyResponseErrorMessage(): EmptyState {
        val errorResponse = kotlin.runCatching {
            Gson().fromJson(
                response()?.errorBody()?.string(),
                DataspikeHttpErrorResponse::class.java
            )
        }.onFailure { throwable -> throwable.printStackTrace() }.getOrNull()

        return EmptyState.EmptyStateError(
            error = errorResponse?.error ?: "Unknown error occurred",
            details = errorResponse?.details ?: "Try again later",
        )
    }
}