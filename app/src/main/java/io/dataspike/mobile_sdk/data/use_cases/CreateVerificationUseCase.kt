package io.dataspike.mobile_sdk.data.use_cases

import io.dataspike.mobile_sdk.data.repository.ISampleAppRepository
import io.dataspike.mobile_sdk.domain.models.NewVerificationState

internal class CreateVerificationUseCase(
    private val sampleAppRepositoryProvider: () -> ISampleAppRepository,
) {

    suspend fun invoke(): NewVerificationState {
        return sampleAppRepositoryProvider.invoke().createVerification()
    }
}