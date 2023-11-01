package io.dataspike.mobile_sdk.domain.mappers

import com.google.gson.Gson
import io.dataspike.mobile_sdk.data.models.responses.UploadImageErrorResponse
import io.dataspike.mobile_sdk.data.models.responses.UploadImageResponse
import io.dataspike.mobile_sdk.domain.models.DataspikeErrorDomainModel
import io.dataspike.mobile_sdk.domain.models.UploadImageState
import retrofit2.HttpException

internal const val ERROR_CODE_EXPIRED = 8000
internal const val ERROR_TOO_MANY_ATTEMPTS = 9000

internal class UploadImageResponseMapper {

    fun map(result: Result<UploadImageResponse>): UploadImageState {
        result
            .onSuccess { uploadImageResponse ->
                return UploadImageState.UploadImageSuccess(
                    documentId = uploadImageResponse.documentId ?: "",
                    detectedDocumentType = uploadImageResponse.detectedDocumentType ?: "",
                    detectedDocumentSide = uploadImageResponse.detectedDocumentSide ?: "",
                    detectedTwoSideDocument = uploadImageResponse.detectedTwoSideDocument ?: false,
                    detectedCountry = uploadImageResponse.detectedCountry ?: "",
                    errors = uploadImageResponse.errors?.map { errorResponse ->
                        DataspikeErrorDomainModel(
                            code = errorResponse.code ?: -1,
                            message = errorResponse.message ?: "",
                        )
                    } ?: emptyList()
                )
            }
            .onFailure { throwable ->
                return (throwable as? HttpException)?.toUploadImageError() ?:
                UploadImageState.UploadImageError(
                    code = -1,
                    message = "Unknown error occurred",
                )
            }

        throw IllegalStateException("Unknown error occurred")
    }

    private fun HttpException.toUploadImageError(): UploadImageState.UploadImageError {
        val uploadImageErrorResponse = kotlin.runCatching {
            Gson().fromJson(
                response()?.errorBody()?.string(),
                UploadImageErrorResponse::class.java
            )
        }.onFailure { throwable -> throwable.printStackTrace() }.getOrNull()

        val errors = uploadImageErrorResponse?.errors
        val isExpiredError = errors?.firstOrNull { error ->
            error.code == ERROR_CODE_EXPIRED
        }
        val tooManyAttemptsError = errors?.firstOrNull { error ->
            error.code == ERROR_TOO_MANY_ATTEMPTS
        }
        val firstError = uploadImageErrorResponse?.errors?.get(0)

        return UploadImageState.UploadImageError(
            code = isExpiredError?.code ?: tooManyAttemptsError?.code ?: firstError?.code ?: -1,
            message = isExpiredError?.message ?: tooManyAttemptsError?.message
                ?: firstError?.message ?: "Unknown error occurred",
        )
    }
}