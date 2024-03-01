package io.dataspike.mobile_sdk.view.view_models

import android.os.CountDownTimer
import io.dataspike.mobile_sdk.data.use_cases.UploadImageUseCase
import io.dataspike.mobile_sdk.domain.models.UploadImageState
import io.dataspike.mobile_sdk.utils.toFile
import io.dataspike.mobile_sdk.view.POI
import io.dataspike.mobile_sdk.view.POI_BACK
import io.dataspike.mobile_sdk.view.POI_FRONT
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

private const val TAKE_PHOTO_WAIT_TIME = 2000L
private const val TICK_INTERVAL = 1000L

internal class LivenessVerificationViewModel(
    private val uploadImageUseCase: UploadImageUseCase,
): BaseViewModel() {

    private val _imageUploadedFlow = MutableSharedFlow<UploadImageState>()
    val imageUploadedFlow: SharedFlow<UploadImageState> = _imageUploadedFlow
    private val _takePhotoFlow = MutableSharedFlow<Boolean>()
    val takePhotoFlow: SharedFlow<Boolean> = _takePhotoFlow
    private var cameraTimer = object : CountDownTimer(
        TAKE_PHOTO_WAIT_TIME,
        TICK_INTERVAL
    ) {

        init {
            showLoading(false)
        }

        override fun onTick(millisUntilFinished: Long) = Unit

        override fun onFinish() {
            launchInVMScope {
                _takePhotoFlow.emit(true)
            }
        }
    }

    fun uploadImage(imageType: String?) {
        launchInVMScope {
            showLoading(true)

            val docType = if (imageType == POI_FRONT || imageType == POI_BACK) {
                POI
            } else {
                imageType ?: ""
            }
            val file = getBitmapFromCache(imageType)?.toFile()
            val uploadImageResult = uploadImageUseCase.invoke(
                docType,
                file ?: return@launchInVMScope
            )

            showLoading(false)
            _imageUploadedFlow.emit(uploadImageResult)
        }
    }

    fun startCameraTimer() {
        cameraTimer.start()
    }

    fun cancelCameraTimer() {
        cameraTimer.cancel()
    }
}