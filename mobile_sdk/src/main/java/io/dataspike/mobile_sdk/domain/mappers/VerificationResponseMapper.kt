package io.dataspike.mobile_sdk.domain.mappers

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import io.dataspike.mobile_sdk.data.models.responses.HttpErrorResponse
import io.dataspike.mobile_sdk.data.models.responses.VerificationResponse
import io.dataspike.mobile_sdk.domain.models.CheckDomainModel
import io.dataspike.mobile_sdk.domain.models.ErrorDomainModel
import io.dataspike.mobile_sdk.domain.models.VerificationChecksDomainModel
import io.dataspike.mobile_sdk.domain.models.VerificationSettingsDomainModel
import io.dataspike.mobile_sdk.domain.models.VerificationState
import retrofit2.HttpException

internal object VerificationResponseMapper {

    fun map(result: Result<VerificationResponse>): VerificationState {
        result
            .onSuccess { verificationResponse ->
                return VerificationState.VerificationSuccess(id = verificationResponse.id ?: "",
                    status = verificationResponse.status ?: "",
                    checks = VerificationChecksDomainModel(
                        faceComparison = CheckDomainModel(
                            status = verificationResponse.checks?.faceComparison?.status ?: "",
                            errors = verificationResponse.checks?.faceComparison?.errors?.map { errorResponse ->
                                ErrorDomainModel(
                                    code = errorResponse.code ?: -1,
                                    message = errorResponse.message ?: "",
                                )
                            } ?: emptyList(),
                            pendingDocuments = verificationResponse.checks?.faceComparison?.pendingDocuments
                                ?: emptyList(),
                        ),
                        liveness = CheckDomainModel(
                            status = verificationResponse.checks?.liveness?.status ?: "",
                            errors = verificationResponse.checks?.liveness?.errors?.map { errorResponse ->
                                ErrorDomainModel(
                                    code = errorResponse.code ?: -1,
                                    message = errorResponse.message ?: "",
                                )
                            } ?: emptyList(),
                            pendingDocuments = verificationResponse.checks?.liveness?.pendingDocuments
                                ?: emptyList(),
                        ),
                        documentMrz = CheckDomainModel(
                            status = verificationResponse.checks?.documentMrz?.status ?: "",
                            errors = verificationResponse.checks?.documentMrz?.errors?.map { errorResponse ->
                                ErrorDomainModel(
                                    code = errorResponse.code ?: -1,
                                    message = errorResponse.message ?: "",
                                )
                            } ?: emptyList(),
                            pendingDocuments = verificationResponse.checks?.documentMrz?.pendingDocuments
                                ?: emptyList(),
                        ),
                        poa = CheckDomainModel(
                            status = verificationResponse.checks?.poa?.status ?: "",
                            errors = verificationResponse.checks?.poa?.errors?.map { errorResponse ->
                                ErrorDomainModel(
                                    code = errorResponse.code ?: -1,
                                    message = errorResponse.message ?: "",
                                )
                            } ?: emptyList(),
                            pendingDocuments = verificationResponse.checks?.poa?.pendingDocuments
                                ?: emptyList(),
                        ),
                    ),
                    redirectUrl = verificationResponse.redirectUrl ?: "",
                    countryCode = verificationResponse.countryCode ?: "",
                    settings = VerificationSettingsDomainModel(
                        poiRequired = verificationResponse.settings?.poiRequired ?: false,
                        poiAllowedDocuments = verificationResponse.settings?.poiAllowedDocuments
                            ?: emptyList(),
                        faceComparisonRequired = verificationResponse.settings?.faceComparisonRequired
                            ?: false,
                        faceComparisonAllowedDocuments = verificationResponse.settings?.faceComparisonAllowedDocuments
                            ?: emptyList(),
                        poaRequired = verificationResponse.settings?.poaRequired ?: false,
                        poaAllowedDocuments = verificationResponse.settings?.poaAllowedDocuments
                            ?: emptyList(),
                        countries = verificationResponse.settings?.countries ?: emptyList(),
                    )
                )
            }
            .onFailure { throwable ->
                return if (throwable is HttpException) {
                    throwable.toGetVerificationErrorMessage()
                } else {
                    VerificationState.VerificationError(
                        error = "Unknown error occurred",
                        details = "Try again later",
                    )
                }
            }

        throw IllegalStateException("Unknown error occurred")
    }

    private fun HttpException.toGetVerificationErrorMessage(): VerificationState {
        val errorResponse = try {
            Gson().fromJson(
                response()?.errorBody()?.string(),
                HttpErrorResponse::class.java
            )
        } catch (e: JsonSyntaxException) {
            null
        }

        return VerificationState.VerificationError(
            error = errorResponse?.error ?: "Unknown error occurred",
            details = errorResponse?.details ?: "Try again later",
        )
    }
}