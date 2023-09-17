package io.dataspike.mobile_sdk.data.use_cases

import io.dataspike.mobile_sdk.data.repository.IDataspikeRepository
import java.io.File

internal class UploadImageUseCase(
    private val dataspikeRepository: IDataspikeRepository,
) {

    suspend operator fun invoke(shortId: String, documentType: String, image: File) =
        dataspikeRepository.uploadImage(shortId, documentType, image)
}