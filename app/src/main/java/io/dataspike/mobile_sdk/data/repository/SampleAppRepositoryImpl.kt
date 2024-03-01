package io.dataspike.mobile_sdk.data.repository

import io.dataspike.mobile_sdk.data.api.ISampleAppApiService
import io.dataspike.mobile_sdk.domain.mappers.NewVerificationResponseMapper
import io.dataspike.mobile_sdk.domain.models.NewVerificationState

internal class SampleAppRepositoryImpl(
    private val sampleAppApiService: ISampleAppApiService,
    private val newVerificationResponseMapper: NewVerificationResponseMapper,
): ISampleAppRepository {

    override suspend fun createVerification(): NewVerificationState {
        return newVerificationResponseMapper.map(
            runCatching {
                sampleAppApiService.createVerification(emptyMap())
            }.onFailure { throwable ->
                throwable.printStackTrace()
            }
        )
    }
}