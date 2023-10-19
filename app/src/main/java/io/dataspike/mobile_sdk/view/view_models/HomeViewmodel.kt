package io.dataspike.mobile_sdk.view.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.dataspike.mobile_sdk.data.use_cases.CreateVerificationUseCase
import io.dataspike.mobile_sdk.dependencies_provider.SampleAppInjector
import io.dataspike.mobile_sdk.dependencies_provider.model.SampleAppDependencies
import io.dataspike.mobile_sdk.domain.mappers.NewVerificationUiMapper
import io.dataspike.mobile_sdk.view.models.NewVerificationUiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

internal class HomeViewModel(
    private val createVerificationUseCase: CreateVerificationUseCase,
    private val newVerificationUiMapper: NewVerificationUiMapper,
): ViewModel() {

    private var dependencies = SampleAppDependencies.DEFAULT

    private val _verificationFlow = MutableSharedFlow<NewVerificationUiState>()
    val verificationFlow: SharedFlow<NewVerificationUiState> = _verificationFlow

    private fun launchInVMScope(
        context: CoroutineContext = Dispatchers.IO,
        block: suspend CoroutineScope.() -> Unit
    ) {
        viewModelScope.launch(context = context, block = block)
    }


    fun createVerification() {
        launchInVMScope {
            SampleAppInjector.setComponent(dependencies)

            if (dependencies.shortId.isEmpty()) {
                _verificationFlow.emit(
                    newVerificationUiMapper.map(
                        createVerificationUseCase.invoke()
                    )
                )
            } else {
                _verificationFlow.emit(
                    NewVerificationUiState.NewVerificationUiSuccess(
                        shortId = dependencies.shortId
                    )
                )
            }
        }
    }

    fun onApiTokenChanged(token: String) {
        dependencies = dependencies.copy(dsApiToken = token)
    }

    fun onIsDebugChanged(checked: Boolean) {
        dependencies = dependencies.copy(isDebug = checked)
    }

    fun onShortIdChanged(shortId: String) {
        dependencies = dependencies.copy(shortId = shortId)
    }
}