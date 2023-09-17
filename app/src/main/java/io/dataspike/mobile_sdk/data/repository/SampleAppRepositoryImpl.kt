package io.dataspike.mobile_sdk.data.repository

import io.dataspike.mobile_sdk.data.api.ISampleAppApiService
import io.dataspike.mobile_sdk.domain.mappers.NewVerificationResponseMapper
import io.dataspike.mobile_sdk.domain.models.NewVerificationState

internal class SampleAppRepositoryImpl(
    private val sampleAppApiService: ISampleAppApiService,
): ISampleAppRepository {

    override suspend fun createVerification(
        body: Map<String, Array<String>>
    ): NewVerificationState = NewVerificationResponseMapper.map(
        runCatching { sampleAppApiService.createVerification(body) }
    )
}