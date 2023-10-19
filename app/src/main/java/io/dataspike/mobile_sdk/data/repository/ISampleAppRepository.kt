package io.dataspike.mobile_sdk.data.repository

import io.dataspike.mobile_sdk.domain.models.NewVerificationState

internal interface ISampleAppRepository {

    suspend fun createVerification(): NewVerificationState
}