package io.dataspike.mobile_sdk.view.view_models

import io.dataspike.mobile_sdk.data.image_caching.ImageCacheManager
import io.dataspike.mobile_sdk.data.use_cases.UploadImageUseCase
import io.dataspike.mobile_sdk.dependencies_provider.DataspikeInjector
import io.dataspike.mobile_sdk.domain.VerificationManager
import io.dataspike.mobile_sdk.domain.models.UploadImageState
import io.dataspike.mobile_sdk.utils.Utils.toFile
import io.dataspike.mobile_sdk.view.POI
import io.dataspike.mobile_sdk.view.POI_BACK
import io.dataspike.mobile_sdk.view.POI_FRONT
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

internal class LivenessVerificationViewModel(
    private val uploadImageUseCase: UploadImageUseCase,
    private val verificationSettingsManager: VerificationManager,
//    private val uiMapper: ImagePreviewUiMapper,
): BaseViewModel() {

//    private val _imagePreviewUiState = MutableSharedFlow<ImagePreviewUiState>()
//    val imagePreviewUiState: SharedFlow<ImagePreviewUiState> = _imagePreviewUiState.asSharedFlow()

    private val _imageUploadedFlow = MutableSharedFlow<UploadImageState>(replay = 1)
    val imageUploadedFlow: SharedFlow<UploadImageState> = _imageUploadedFlow

    fun uploadImage(dir: String, imageType: String?) {
        launchInVMScope {
            showLoading(true)

            val docType = if (imageType == POI_FRONT || imageType == POI_BACK) {
                POI
            } else {
                imageType ?: ""
            }
            val file =
                ImageCacheManager.getBitmapFromCache(imageType)
                    ?.toFile("${System.currentTimeMillis()}_DS", dir)

            val uploadImageResult = uploadImageUseCase.invoke(
                DataspikeInjector.component.shortId,
                docType,
                file ?: return@launchInVMScope
            )
            showLoading(false)
            _imageUploadedFlow.emit(uploadImageResult)
//            _imagePreviewUiState.emit(uiMapper.toUiState(uploadImageResult))
        }
    }
}