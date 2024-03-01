package io.dataspike.mobile_sdk.domain.mappers

import com.google.gson.Gson
import io.dataspike.mobile_sdk.data.models.responses.NewVerificationErrorResponse
import io.dataspike.mobile_sdk.data.models.responses.NewVerificationResponse
import io.dataspike.mobile_sdk.domain.models.CheckDomainModel
import io.dataspike.mobile_sdk.domain.models.ErrorDomainModel
import io.dataspike.mobile_sdk.domain.models.NewVerificationState
import io.dataspike.mobile_sdk.domain.models.VerificationChecksDomainModel
import retrofit2.HttpException

internal class NewVerificationResponseMapper {

    fun map(result: Result<NewVerificationResponse>): NewVerificationState {
        result
            .onSuccess { newVerificationResponse ->
                return NewVerificationState.NewVerificationSuccess(
                    id = newVerificationResponse.id ?: "",
                    status = newVerificationResponse.status ?: "",
                    checks = VerificationChecksDomainModel(
                        faceComparison = CheckDomainModel(
                            status = newVerificationResponse.checks?.faceComparison?.status ?: "",
                            errors = newVerificationResponse.checks?.faceComparison?.errors?.map { errorResponse ->
                                ErrorDomainModel(
                                    code = errorResponse.code ?: -1,
                                    message = errorResponse.message ?: "",
                                )
                            } ?: emptyList(),
                            pendingDocuments = newVerificationResponse.checks?.faceComparison?.pendingDocuments
                                ?: emptyList(),
                        ),
                        liveness = CheckDomainModel(
                            status = newVerificationResponse.checks?.liveness?.status ?: "",
                            errors = newVerificationResponse.checks?.liveness?.errors?.map { errorResponse ->
                                ErrorDomainModel(
                                    code = errorResponse.code ?: -1,
                                    message = errorResponse.message ?: "",
                                )
                            } ?: emptyList(),
                            pendingDocuments = newVerificationResponse.checks?.liveness?.pendingDocuments
                                ?: emptyList(),
                        ),
                        documentMrz = CheckDomainModel(
                            status = newVerificationResponse.checks?.documentMrz?.status ?: "",
                            errors = newVerificationResponse.checks?.documentMrz?.errors?.map { errorResponse ->
                                ErrorDomainModel(
                                    code = errorResponse.code ?: -1,
                                    message = errorResponse.message ?: "",
                                )
                            } ?: emptyList(),
                            pendingDocuments = newVerificationResponse.checks?.documentMrz?.pendingDocuments
                                ?: emptyList(),
                        ),
                        poa = CheckDomainModel(
                            status = newVerificationResponse.checks?.poa?.status ?: "",
                            errors = newVerificationResponse.checks?.poa?.errors?.map { errorResponse ->
                                ErrorDomainModel(
                                    code = errorResponse.code ?: -1,
                                    message = errorResponse.message ?: "",
                                )
                            } ?: emptyList(),
                            pendingDocuments = newVerificationResponse.checks?.poa?.pendingDocuments
                                ?: emptyList(),
                        ),
                    ),
                    createdAt = newVerificationResponse.createdAt ?: "",
                    isSandbox = newVerificationResponse.isSandbox ?: false,
                    verificationUrl = newVerificationResponse.verificationUrl ?: "",
                    verificationUrlId = newVerificationResponse.verificationUrlId ?: "",
                    expiresAt = newVerificationResponse.expiresAt ?: "",
                )
            }
            .onFailure { throwable ->
                return if (throwable is HttpException) {
                    throwable.toNewVerificationErrorMessage()
                } else {
                    NewVerificationState.NewVerificationError(
                        validationError = "Unknown error occurred",
                        error = "Try again later",
                    )
                }
            }

        throw IllegalStateException("Unknown error occurred")
    }

    private fun HttpException.toNewVerificationErrorMessage(): NewVerificationState {
        val errorResponse = kotlin.runCatching {
            Gson().fromJson(
                response()?.errorBody()?.string(),
                NewVerificationErrorResponse::class.java
            )
        }.onFailure { throwable ->
            throwable.printStackTrace()
        }.getOrNull()

        return NewVerificationState.NewVerificationError(
            validationError = errorResponse?.validationErrors?.get(0) ?: "Unknown error occurred",
            error = errorResponse?.error ?: "Try again later",
        )
    }
}