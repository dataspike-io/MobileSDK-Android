package io.dataspike.mobile_sdk.view.mappers

import io.dataspike.mobile_sdk.dependencies_provider.DataspikeInjector
import io.dataspike.mobile_sdk.domain.models.UploadImageState
import io.dataspike.mobile_sdk.view.LIVENESS
import io.dataspike.mobile_sdk.view.POA
import io.dataspike.mobile_sdk.view.POI_BACK
import io.dataspike.mobile_sdk.view.POI_FRONT
import io.dataspike.mobile_sdk.view.VERIFICATION_COMPLETED
import io.dataspike.mobile_sdk.view.ui_models.UploadImageUiState

internal const val ERROR_CODE_EXPIRED = 8000
internal const val ERROR_TOO_MANY_ATTEMPTS = 9000

internal class UploadImageUiMapper {

    fun map(imageType: String?, uploadImageState: UploadImageState): UploadImageUiState {
        val checks = DataspikeInjector.component.verificationManager.checks

        return when (uploadImageState) {
            is UploadImageState.UploadImageSuccess -> {
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
                                    VERIFICATION_COMPLETED
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
                                VERIFICATION_COMPLETED
                            }
                        }
                    }

                    LIVENESS -> {
                        if (checks.poaIsRequired) {
                            POA
                        } else {
                            VERIFICATION_COMPLETED
                        }
                    }

                    POA -> {
                        VERIFICATION_COMPLETED
                    }

                    else -> {
                        VERIFICATION_COMPLETED
                    }
                }

                UploadImageUiState.UploadImageUiSuccess(
                    shouldNavigateToSelectCountryFragment =
                    imageType == POI_FRONT && uploadImageState.detectedCountry.isEmpty(),
                    fragmentToNavigateToOnContinue = fragmentToNavigateToOnContinue,
                )
            }

            is UploadImageState.UploadImageError -> {
                val fragmentToNavigateToOnContinue = when (imageType) {
                    POI_FRONT, POI_BACK -> {
                        when {
                            checks.livenessIsRequired -> {
                                LIVENESS
                            }

                            checks.poaIsRequired -> {
                                POA
                            }

                            else -> {
                                VERIFICATION_COMPLETED
                            }
                        }
                    }

                    LIVENESS -> {
                        if (checks.poaIsRequired) {
                            POA
                        } else {
                            VERIFICATION_COMPLETED
                        }
                    }

                    POA -> {
                        VERIFICATION_COMPLETED
                    }

                    else -> {
                        VERIFICATION_COMPLETED
                    }
                }

                UploadImageUiState.UploadImageUiError(
                    isExpired = uploadImageState.code == ERROR_CODE_EXPIRED,
                    tooManyAttempts = uploadImageState.code == ERROR_TOO_MANY_ATTEMPTS,
                    errorMessage = uploadImageState.message,
                    fragmentToNavigateTo = fragmentToNavigateToOnContinue,
                )
            }
        }
    }
}