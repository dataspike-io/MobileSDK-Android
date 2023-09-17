package io.dataspike.mobile_sdk.data.repository

import io.dataspike.mobile_sdk.data.api.IDataspikeApiService
import io.dataspike.mobile_sdk.domain.mappers.EmptyStateMapper
import io.dataspike.mobile_sdk.domain.mappers.UploadImageResponseMapper
import io.dataspike.mobile_sdk.domain.mappers.VerificationResponseMapper
import io.dataspike.mobile_sdk.domain.models.EmptyState
import io.dataspike.mobile_sdk.domain.models.UploadImageState
import io.dataspike.mobile_sdk.domain.models.VerificationState
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

internal class DataspikeRepositoryImpl(
    private val dataspikeApiService: IDataspikeApiService,
): IDataspikeRepository {

    override suspend fun getVerification(
        shortId: String,
    ): VerificationState = VerificationResponseMapper.map(
        runCatching { dataspikeApiService.getVerification(shortId) }
    )

    override suspend fun uploadImage(
        shortId: String,
        documentType: String,
        image: File,
    ): UploadImageState {
        val requestBody = image.asRequestBody("image/*".toMediaType())
        val multiPart = MultipartBody.Part.createFormData("file", image.name, requestBody)
        val rT = documentType.toRequestBody("text/plain".toMediaType())

        return UploadImageResponseMapper.map(
            runCatching { dataspikeApiService.uploadImage(shortId, rT, multiPart) }
        )
    }

    override suspend fun proceedWithVerification(
        shortId: String,
        body: Map<String, String>,
    ): EmptyState = EmptyStateMapper.map(
        runCatching { dataspikeApiService.proceedWithVerification(shortId, body) }
    )
}