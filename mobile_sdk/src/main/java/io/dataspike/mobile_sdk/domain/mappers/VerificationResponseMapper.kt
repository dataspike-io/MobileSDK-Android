package io.dataspike.mobile_sdk.domain.mappers

import io.dataspike.mobile_sdk.data.models.responses.DataspikeHttpErrorResponse
import io.dataspike.mobile_sdk.data.models.responses.VerificationResponse
import io.dataspike.mobile_sdk.domain.models.DataspikeCheckDomainModel
import io.dataspike.mobile_sdk.domain.models.DataspikeErrorDomainModel
import io.dataspike.mobile_sdk.domain.models.DataspikeVerificationChecksDomainModel
import io.dataspike.mobile_sdk.domain.models.UiConfigDomainModel
import io.dataspike.mobile_sdk.domain.models.VerificationSettingsDomainModel
import io.dataspike.mobile_sdk.domain.models.VerificationState
import io.dataspike.mobile_sdk.utils.deserializeFromJson
import io.dataspike.mobile_sdk.view.mappers.UiConfigMapper
import io.dataspike.mobile_sdk.view.ui_models.UiConfigModel
import retrofit2.HttpException

internal class VerificationResponseMapper {

    private fun HttpException.toGetVerificationErrorMessage(): VerificationState {
        val errorResponse =
            response()
                ?.errorBody()
                ?.string()
                .deserializeFromJson(DataspikeHttpErrorResponse::class.java)

        return VerificationState.VerificationError(
            error = errorResponse?.error ?: "Unknown error occurred",
            details = errorResponse?.details ?: "Try again later",
        )
    }

    private fun String?.toUiConfigModel(darkModeIsEnabled: Boolean): UiConfigModel {
        val uiConfigDomainModel = deserializeFromJson(UiConfigDomainModel::class.java)

        return UiConfigMapper().map(uiConfigDomainModel, darkModeIsEnabled)
    }

    fun map(result: Result<VerificationResponse>, darkModeIsEnabled: Boolean): VerificationState {
        result
            .onSuccess { verificationResponse ->
                return VerificationState.VerificationSuccess(
                    id = verificationResponse.id ?: "",
                    status = verificationResponse.status ?: "",
                    checks = DataspikeVerificationChecksDomainModel(
                        faceComparison = DataspikeCheckDomainModel(
                            status = verificationResponse.checks?.faceComparison?.status ?: "",
                            errors = verificationResponse
                                .checks
                                ?.faceComparison
                                ?.errors
                                ?.map { errorResponse ->
                                DataspikeErrorDomainModel(
                                    code = errorResponse.code ?: -1,
                                    message = errorResponse.message ?: "",
                                )
                            } ?: emptyList(),
                            pendingDocuments =
                            verificationResponse.checks?.faceComparison?.pendingDocuments
                                ?: emptyList(),
                        ),
                        liveness = DataspikeCheckDomainModel(
                            status = verificationResponse.checks?.liveness?.status ?: "",
                            errors =
                            verificationResponse.checks?.liveness?.errors?.map { errorResponse ->
                                DataspikeErrorDomainModel(
                                    code = errorResponse.code ?: -1,
                                    message = errorResponse.message ?: "",
                                )
                            } ?: emptyList(),
                            pendingDocuments =
                            verificationResponse.checks?.liveness?.pendingDocuments ?: emptyList(),
                        ),
                        documentMrz = DataspikeCheckDomainModel(
                            status = verificationResponse.checks?.documentMrz?.status ?: "",
                            errors =
                            verificationResponse.checks?.documentMrz?.errors?.map { errorResponse ->
                                DataspikeErrorDomainModel(
                                    code = errorResponse.code ?: -1,
                                    message = errorResponse.message ?: "",
                                )
                            } ?: emptyList(),
                            pendingDocuments =
                            verificationResponse.checks?.documentMrz?.pendingDocuments
                                ?: emptyList(),
                        ),
                        poa = DataspikeCheckDomainModel(
                            status = verificationResponse.checks?.poa?.status ?: "",
                            errors =
                            verificationResponse.checks?.poa?.errors?.map { errorResponse ->
                                DataspikeErrorDomainModel(
                                    code = errorResponse.code ?: -1,
                                    message = errorResponse.message ?: "",
                                )
                            } ?: emptyList(),
                            pendingDocuments = verificationResponse.checks?.poa?.pendingDocuments
                                ?: emptyList(),
                        ),
                    ),
                    verificationUrl = verificationResponse.verificationUrl ?: "",
                    countryCode = verificationResponse.countryCode ?: "",
                    settings = VerificationSettingsDomainModel(
                        poiRequired = verificationResponse.settings?.poiRequired ?: false,
                        poiAllowedDocuments = verificationResponse.settings?.poiAllowedDocuments
                            ?: emptyList(),
                        faceComparisonRequired =
                        verificationResponse.settings?.faceComparisonRequired ?: false,
                        faceComparisonAllowedDocuments =
                        verificationResponse.settings?.faceComparisonAllowedDocuments
                            ?: emptyList(),
                        poaRequired = verificationResponse.settings?.poaRequired ?: false,
                        poaAllowedDocuments = verificationResponse.settings?.poaAllowedDocuments
                            ?: emptyList(),
                        countries = verificationResponse.settings?.countries ?: emptyList(),
                        uiConfig =
                        verificationResponse
                            .settings
                            ?.uiSettings
                            .toUiConfigModel(darkModeIsEnabled),
                    ),
                     expiresAt = verificationResponse.expiresAt ?: "",
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
}