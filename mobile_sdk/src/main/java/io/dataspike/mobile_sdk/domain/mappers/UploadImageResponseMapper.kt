package io.dataspike.mobile_sdk.domain.mappers

import com.google.gson.Gson
import io.dataspike.mobile_sdk.data.models.responses.UploadImageErrorResponse
import io.dataspike.mobile_sdk.data.models.responses.UploadImageResponse
import io.dataspike.mobile_sdk.domain.models.ErrorDomainModel
import io.dataspike.mobile_sdk.domain.models.UploadImageState
import retrofit2.HttpException

internal const val ERROR_CODE_EXPIRED = 8000

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
                        ErrorDomainModel(
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
        val expiredError = errors?.firstOrNull { error -> error.code == ERROR_CODE_EXPIRED }
        val firstError = uploadImageErrorResponse?.errors?.get(0)

        return UploadImageState.UploadImageError(
            code = expiredError?.code ?: firstError?.code ?: -1,
            message = expiredError?.message ?: firstError?.message ?: "Unknown error occurred"
        )
    }
}