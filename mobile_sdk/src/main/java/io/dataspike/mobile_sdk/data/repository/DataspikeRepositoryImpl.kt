package io.dataspike.mobile_sdk.data.repository

import io.dataspike.mobile_sdk.data.api.IDataspikeApiService
import io.dataspike.mobile_sdk.data.models.requests.CountryRequestBody
import io.dataspike.mobile_sdk.domain.mappers.CountriesResponseMapper
import io.dataspike.mobile_sdk.domain.mappers.EmptyResponseMapper
import io.dataspike.mobile_sdk.domain.mappers.ProceedWithVerificationResponseMapper
import io.dataspike.mobile_sdk.domain.mappers.UploadImageResponseMapper
import io.dataspike.mobile_sdk.domain.mappers.VerificationResponseMapper
import io.dataspike.mobile_sdk.domain.models.CountriesState
import io.dataspike.mobile_sdk.domain.models.EmptyState
import io.dataspike.mobile_sdk.domain.models.ProceedWithVerificationState
import io.dataspike.mobile_sdk.domain.models.UploadImageState
import io.dataspike.mobile_sdk.domain.models.VerificationState
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

internal class DataspikeRepositoryImpl(
    private val dataspikeApiService: IDataspikeApiService,
    private val shortId: String,
    private val verificationResponseMapper: VerificationResponseMapper,
    private val uploadImageResponseMapper: UploadImageResponseMapper,
    private val countriesResponseMapper: CountriesResponseMapper,
    private val emptyResponseMapper: EmptyResponseMapper,
    private val proceedWithVerificationResponseMapper: ProceedWithVerificationResponseMapper,
) : IDataspikeRepository {

    override suspend fun getVerification(): VerificationState {
        return verificationResponseMapper.map(
            runCatching { dataspikeApiService.getVerification(shortId) }
        )
    }

    override suspend fun uploadImage(
        documentType: String,
        image: File,
    ): UploadImageState {
        val requestBody = image.asRequestBody("image/*".toMediaType())
        val multiPart = MultipartBody.Part.createFormData("file", image.name, requestBody)
        val documentTypeRequestBody = documentType.toRequestBody("text/plain".toMediaType())

        return uploadImageResponseMapper.map(
            runCatching {
                dataspikeApiService.uploadImage(shortId, documentTypeRequestBody, multiPart)
            }
        )
    }

    override suspend fun getCountries(): CountriesState {
        return countriesResponseMapper.map(
            runCatching { dataspikeApiService.getCountries() }
        )
    }

    override suspend fun setCountry(
        body: CountryRequestBody,
    ): EmptyState {
        return emptyResponseMapper.map(
            runCatching { dataspikeApiService.setCountry(shortId, body) }
        )
    }

    override suspend fun proceedWithVerification(): ProceedWithVerificationState {
        return proceedWithVerificationResponseMapper.map(
            runCatching { dataspikeApiService.proceedWithVerification(shortId) }
        )
    }
}