package io.dataspike.mobile_sdk.view.view_models

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.dataspike.mobile_sdk.data.image_caching.ImageCacheManager
import io.dataspike.mobile_sdk.dependencies_provider.DataspikeInjector
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

internal open class BaseViewModel: ViewModel() {

    private val imageCacheManager: ImageCacheManager = DataspikeInjector.component.imageCacheManager
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

    fun putBitmapIntoCache(key: String, bitmap: Bitmap?) {
        imageCacheManager.putBitmapIntoCache(key, bitmap)
    }

    fun getBitmapFromCache(key: String?): Bitmap? {
        return imageCacheManager.getBitmapFromCache(key)
    }
}