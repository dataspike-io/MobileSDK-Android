package io.dataspike.mobile_sdk.view.view_models

import io.dataspike.mobile_sdk.data.use_cases.GetVerificationUseCase
import io.dataspike.mobile_sdk.dependencies_provider.DataspikeInjector
import io.dataspike.mobile_sdk.domain.models.VerificationState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

internal class DataspikeActivityViewModel(
    private val getVerificationUseCase: GetVerificationUseCase
): BaseViewModel() {

    private val _verificationFlow = MutableSharedFlow<VerificationState>(replay = 1)
    val verificationFlow: SharedFlow<VerificationState> = _verificationFlow

    fun getVerification() {
        launchInVMScope {
            _verificationFlow.emit(getVerificationUseCase(DataspikeInjector.component.shortId))
        }
    }
}