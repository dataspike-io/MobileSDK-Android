package io.dataspike.mobile_sdk.view.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.dataspike.mobile_sdk.data.use_cases.GetCountriesUseCase
import io.dataspike.mobile_sdk.data.use_cases.GetVerificationUseCase
import io.dataspike.mobile_sdk.data.use_cases.ProceedWithVerificationUseCase
import io.dataspike.mobile_sdk.data.use_cases.SetCountryUseCase
import io.dataspike.mobile_sdk.data.use_cases.UploadImageUseCase
import io.dataspike.mobile_sdk.dependencies_provider.DataspikeInjector

internal class DataspikeViewModelFactory: ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when (modelClass) {
            DataspikeActivityViewModel::class.java -> {
                DataspikeActivityViewModel(
                    getVerificationUseCase = GetVerificationUseCase(
                        dataspikeRepository =
                        DataspikeInjector.component.dataspikeRepository,
                        verificationSettingsManager =
                        DataspikeInjector.component.verificationManager,
                    ),
                ) as T
            }

            BaseViewModel::class.java -> {
                BaseViewModel() as T
            }

            OnboardingViewModel::class.java -> {
                OnboardingViewModel() as T
            }

            ImagePreviewViewModel::class.java -> {
                ImagePreviewViewModel(
                    uploadImageUseCase = UploadImageUseCase(
                        dataspikeRepository = DataspikeInjector.component.dataspikeRepository,
                    ),
                    uploadImageUiMapper = DataspikeInjector.component.uploadImageUiMapper,
                ) as T
            }

            VerificationCompleteViewModel::class.java -> {
                VerificationCompleteViewModel(
                    proceedWithVerificationUseCase = ProceedWithVerificationUseCase(
                        dataspikeRepository = DataspikeInjector.component.dataspikeRepository,
                    ),
                    proceedWithVerificationUiMapper =
                    DataspikeInjector.component.proceedWithVerificationUiMapper,
                ) as T
            }

            SelectCountryViewModel::class.java -> {
                SelectCountryViewModel(
                    getCountriesUseCase = GetCountriesUseCase(
                        dataspikeRepository = DataspikeInjector.component.dataspikeRepository,
                    ),
                    SetCountryUseCase(
                        dataspikeRepository = DataspikeInjector.component.dataspikeRepository,
                    ),
                ) as T
            }

            LivenessVerificationViewModel::class.java -> {
                LivenessVerificationViewModel(
                    uploadImageUseCase = UploadImageUseCase(
                        dataspikeRepository = DataspikeInjector.component.dataspikeRepository,

                        ),
                ) as T
            }

            else -> {
                throw Exception("Unknown ViewModel Type")
            }
        }
    }
}