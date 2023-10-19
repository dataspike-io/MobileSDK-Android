package io.dataspike.mobile_sdk.view.mappers

import io.dataspike.mobile_sdk.dependencies_provider.DataspikeInjector
import io.dataspike.mobile_sdk.domain.models.UploadImageState
import io.dataspike.mobile_sdk.view.LIVENESS
import io.dataspike.mobile_sdk.view.POA
import io.dataspike.mobile_sdk.view.POI_BACK
import io.dataspike.mobile_sdk.view.POI_FRONT
import io.dataspike.mobile_sdk.view.VERIFICATION_COMPLETE
import io.dataspike.mobile_sdk.view.ui_models.UploadImageUiState

internal const val ERROR_CODE_EXPIRED = 8000

internal class UploadImageUiMapper {

    fun map(imageType: String?, uploadImageState: UploadImageState): UploadImageUiState {
        return when (uploadImageState) {
            is UploadImageState.UploadImageSuccess -> {
                val checks = DataspikeInjector.component.verificationManager.checks

                val fragmentToNavigateToOnContinue = when (imageType) {
                    POI_FRONT -> {
                        if (uploadImageState.detectedTwoSideDocument) {
                            POI_BACK
                        } else {
                            when {
                                checks.livenessIsRequired -> {
                                    LIVENESS
                                }

                                checks.poaIsRequired -> {
                                    POA
                                }

                                else -> {
                                    VERIFICATION_COMPLETE
                                }
                            }
                        }
                    }

                    POI_BACK -> {
                        when {
                            checks.livenessIsRequired -> {
                                LIVENESS
                            }

                            checks.poaIsRequired -> {
                                POA
                            }

                            else -> {
                                VERIFICATION_COMPLETE
                            }
                        }
                    }

                    LIVENESS -> {
                        if (checks.poaIsRequired) {
                            POA
                        } else {
                            VERIFICATION_COMPLETE
                        }
                    }

                    POA -> {
                        VERIFICATION_COMPLETE
                    }

                    else -> {
                        VERIFICATION_COMPLETE
                    }
                }

                UploadImageUiState.UploadImageUiSuccess(
                    shouldNavigateToSelectCountryFragment =
                    imageType == POI_FRONT && uploadImageState.detectedCountry.isEmpty(),
                    fragmentToNavigateToOnContinue = fragmentToNavigateToOnContinue,
                )
            }

            is UploadImageState.UploadImageError -> {
                UploadImageUiState.UploadImageUiError(
                    shouldNavigateToVerificationExpiredFragment =
                    uploadImageState.code == ERROR_CODE_EXPIRED,
                    errorMessage = uploadImageState.message,
                )
            }
        }
    }
}