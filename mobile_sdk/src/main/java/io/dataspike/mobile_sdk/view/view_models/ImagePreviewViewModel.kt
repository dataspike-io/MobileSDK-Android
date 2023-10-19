package io.dataspike.mobile_sdk.view.view_models

import io.dataspike.mobile_sdk.data.use_cases.UploadImageUseCase
import io.dataspike.mobile_sdk.utils.toFile
import io.dataspike.mobile_sdk.view.POI
import io.dataspike.mobile_sdk.view.POI_BACK
import io.dataspike.mobile_sdk.view.POI_FRONT
import io.dataspike.mobile_sdk.view.mappers.UploadImageUiMapper
import io.dataspike.mobile_sdk.view.ui_models.UploadImageUiState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

internal class ImagePreviewViewModel(
    private val uploadImageUseCase: UploadImageUseCase,
    private val uploadImageUiMapper: UploadImageUiMapper,
): BaseViewModel() {

    private val _imageUploadedFlow = MutableSharedFlow<UploadImageUiState>()
    val imageUploadedFlow: SharedFlow<UploadImageUiState> = _imageUploadedFlow

    fun uploadImage(imageType: String?) {
        launchInVMScope {
            showLoading(true)

            val docType = if (imageType == POI_FRONT || imageType == POI_BACK) {
                POI
            } else {
                imageType ?: ""
            }
            val file = getBitmapFromCache(imageType)?.toFile()

            _imageUploadedFlow.emit(
                uploadImageUiMapper.map(
                    imageType,
                    uploadImageUseCase.invoke(
                        docType,
                        file ?: return@launchInVMScope
                    )
                )
            )

            showLoading(false)
        }
    }
}