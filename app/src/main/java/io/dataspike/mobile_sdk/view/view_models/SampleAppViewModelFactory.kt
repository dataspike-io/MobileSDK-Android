package io.dataspike.mobile_sdk.view.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.dataspike.mobile_sdk.data.use_cases.CreateVerificationUseCase
import io.dataspike.mobile_sdk.dependencies_provider.SampleAppInjector

internal class SampleAppViewModelFactory: ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        when (modelClass) {
            HomeViewModel::class.java -> {
                HomeViewModel(
                    createVerificationUseCase =
                    CreateVerificationUseCase { SampleAppInjector.component.sampleAppRepository },
                    newVerificationUiMapper = SampleAppInjector.component.newVerificationUiMapper,
                ) as T
            }

            else -> {
                throw Exception("Unknown ViewModel Type")
            }
        }
}