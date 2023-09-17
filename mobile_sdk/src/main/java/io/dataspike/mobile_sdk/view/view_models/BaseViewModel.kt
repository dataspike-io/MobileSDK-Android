package io.dataspike.mobile_sdk.view.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

internal open class BaseViewModel: ViewModel() {

    private val _loadingFlow = MutableSharedFlow<Boolean>(replay = 1)
    val loadingFlow = _loadingFlow

    fun launchInVMScope(
        context: CoroutineContext = Dispatchers.IO,
        block: suspend CoroutineScope.() -> Unit
    ) {
        viewModelScope.launch(context = context, block = block)
    }

    fun showLoading(show: Boolean) {
        _loadingFlow.tryEmit(show)
    }
}