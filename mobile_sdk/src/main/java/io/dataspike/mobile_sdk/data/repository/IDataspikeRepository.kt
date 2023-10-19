package io.dataspike.mobile_sdk.data.repository

import io.dataspike.mobile_sdk.data.models.requests.CountryRequestBody
import io.dataspike.mobile_sdk.domain.models.CountriesState
import io.dataspike.mobile_sdk.domain.models.EmptyState
import io.dataspike.mobile_sdk.domain.models.ProceedWithVerificationState
import io.dataspike.mobile_sdk.domain.models.UploadImageState
import io.dataspike.mobile_sdk.domain.models.VerificationState
import java.io.File

internal interface IDataspikeRepository {

    suspend fun getVerification(): VerificationState

    suspend fun uploadImage(documentType: String, image: File): UploadImageState

    suspend fun setCountry(body: CountryRequestBody): EmptyState

    suspend fun getCountries(): CountriesState

    suspend fun proceedWithVerification(): ProceedWithVerificationState
}