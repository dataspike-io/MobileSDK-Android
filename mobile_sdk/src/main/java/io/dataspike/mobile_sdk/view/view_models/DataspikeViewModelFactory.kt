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
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        when (modelClass) {
            DataspikeActivityViewModel::class.java -> {
                DataspikeActivityViewModel(
                    GetVerificationUseCase(
                        DataspikeInjector.component.dataspikeRepository,
                        DataspikeInjector.component.verificationSettingsManager,
                    ),
                ) as T
            }

            ImagePreviewViewModel::class.java -> {
                ImagePreviewViewModel(
                    UploadImageUseCase(
                        DataspikeInjector.component.dataspikeRepository,

                    ),
                    DataspikeInjector.component.verificationSettingsManager,
                ) as T
            }

            VerificationCompleteViewModel::class.java -> {
                VerificationCompleteViewModel(
                    ProceedWithVerificationUseCase(
                        DataspikeInjector.component.dataspikeRepository,
                    ),
                ) as T
            }

            SelectCountryViewModel::class.java -> {
                SelectCountryViewModel(
                    GetCountriesUseCase(
                        DataspikeInjector.component.dataspikeRepository,
                    ),
                    SetCountryUseCase(
                        DataspikeInjector.component.dataspikeRepository,
                    ),
                ) as T
            }

            LivenessVerificationViewModel::class.java -> {
                LivenessVerificationViewModel(
                    UploadImageUseCase(
                        DataspikeInjector.component.dataspikeRepository,

                        ),
                    DataspikeInjector.component.verificationSettingsManager,
                ) as T
            }

            else -> {
                throw Exception("Unknown ViewModel Type")
            }
        }
}