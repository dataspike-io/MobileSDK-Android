package io.dataspike.mobile_sdk.domain.mappers

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import io.dataspike.mobile_sdk.data.models.responses.UploadImageErrorResponse
import io.dataspike.mobile_sdk.data.models.responses.UploadImageResponse
import io.dataspike.mobile_sdk.domain.models.ErrorDomainModel
import io.dataspike.mobile_sdk.domain.models.UploadImageState
import retrofit2.HttpException

internal object UploadImageResponseMapper {

    fun map(result: Result<UploadImageResponse>): UploadImageState {
        result
            .onSuccess { uploadImageResponse ->
                return UploadImageState.UploadImageSuccess(
                    documentId = uploadImageResponse.documentId ?: "",
                    detectedDocumentType = uploadImageResponse.detectedDocumentType ?: "",
                    detectedDocumentSide = uploadImageResponse.detectedDocumentSide ?: "",
                    detectedTwoSideDocument = uploadImageResponse.detectedTwoSideDocument ?: false,
                    errors = uploadImageResponse.errors?.map { errorResponse ->
                        ErrorDomainModel(
                            code = errorResponse.code ?: -1,
                            message = errorResponse.message ?: "",
                        )
                    } ?: emptyList()
                )
            }
            .onFailure { throwable ->
                val message = when (throwable) {
                    is HttpException -> {
                        throwable.toUploadImageErrorMessage()
                    }

                    else -> "Unknown error occurred"
                }

                return UploadImageState.UploadImageError(
                    message = message
                )
            }

        throw IllegalStateException("Unknown error occurred")
    }

    private fun HttpException.toUploadImageErrorMessage(): String {
        val uploadImageErrorResponse = try {
            Gson().fromJson(
                response()?.errorBody()?.string(),
                UploadImageErrorResponse::class.java
            )
        } catch (e: JsonSyntaxException) {
            null
        }

        return uploadImageErrorResponse?.errors?.get(0)?.message?.replaceFirstChar { char ->
            if (char.isLowerCase()) {
                char.titlecase()
            } else {
                char.toString()
            }
        } ?: "Unknown error occurred"
    }
}