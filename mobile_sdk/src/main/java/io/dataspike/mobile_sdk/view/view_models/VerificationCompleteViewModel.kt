package io.dataspike.mobile_sdk.view.view_models

import io.dataspike.mobile_sdk.data.use_cases.ProceedWithVerificationUseCase
import io.dataspike.mobile_sdk.view.mappers.ProceedWithVerificationUiMapper
import io.dataspike.mobile_sdk.view.ui_models.ProceedWithVerificationUiState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

internal class VerificationCompleteViewModel(
    private val proceedWithVerificationUseCase: ProceedWithVerificationUseCase,
    private val proceedWithVerificationUiMapper: ProceedWithVerificationUiMapper,
): BaseViewModel() {

    private val _proceedWithVerificationFlow =
        MutableSharedFlow<ProceedWithVerificationUiState>(replay = 1)
    val proceedWithVerificationFlow: SharedFlow<ProceedWithVerificationUiState> =
        _proceedWithVerificationFlow

    fun proceedWithVerification() {
        launchInVMScope {
            _proceedWithVerificationFlow.emit(
                proceedWithVerificationUiMapper.map(
                    proceedWithVerificationUseCase.invoke()
                )
            )
        }
    }
}