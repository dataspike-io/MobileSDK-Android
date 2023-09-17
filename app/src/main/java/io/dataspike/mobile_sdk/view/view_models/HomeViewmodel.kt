package io.dataspike.mobile_sdk.view.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.dataspike.mobile_sdk.data.use_cases.CreateVerificationUseCase
import io.dataspike.mobile_sdk.domain.models.NewVerificationState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

internal class HomeViewModel(
    private val createVerificationUseCase: CreateVerificationUseCase,
): ViewModel() {

    private val _verificationFlow = MutableSharedFlow<NewVerificationState>(replay = 1)
    val verificationFlow: SharedFlow<NewVerificationState> = _verificationFlow

    private fun launchInVMScope(
        context: CoroutineContext = Dispatchers.IO,
        block: suspend CoroutineScope.() -> Unit
    ) {
        viewModelScope.launch(context = context, block = block)
    }

    fun createVerification(body: Map<String, Array<String>>) {
        launchInVMScope {
            _verificationFlow.emit(createVerificationUseCase.invoke(body))
        }
    }
}