package io.dataspike.mobile_sdk.view.view_models

import io.dataspike.mobile_sdk.data.use_cases.ProceedWithVerificationUseCase
import io.dataspike.mobile_sdk.dependencies_provider.DataspikeInjector
import io.dataspike.mobile_sdk.domain.models.EmptyState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

internal class VerificationCompleteViewModel(
    private val proceedWithVerificationUseCase: ProceedWithVerificationUseCase,
): BaseViewModel() {

    private val _proceedWithVerificationFlow = MutableSharedFlow<EmptyState>(replay = 1)
    val proceedWithVerificationFlow: SharedFlow<EmptyState> = _proceedWithVerificationFlow

    fun proceedWithVerification() {
        launchInVMScope {
            DataspikeInjector.component.shortId?.let { shortId ->
                _proceedWithVerificationFlow.emit(
                    proceedWithVerificationUseCase.invoke("shortId")
                )
            }
        }
    }
}