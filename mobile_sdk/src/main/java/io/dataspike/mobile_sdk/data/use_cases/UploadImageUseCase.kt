package io.dataspike.mobile_sdk.data.use_cases

import io.dataspike.mobile_sdk.data.repository.IDataspikeRepository
import io.dataspike.mobile_sdk.domain.models.UploadImageState
import java.io.File

internal class UploadImageUseCase(
    private val dataspikeRepository: IDataspikeRepository,
) {

    suspend operator fun invoke(documentType: String, image: File): UploadImageState {
        return dataspikeRepository.uploadImage(documentType, image)
    }
}