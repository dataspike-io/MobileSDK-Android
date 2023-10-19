package io.dataspike.mobile_sdk.data.api

import io.dataspike.mobile_sdk.data.models.responses.NewVerificationResponse
import retrofit2.http.Body
import retrofit2.http.POST

internal interface ISampleAppApiService {

    @POST("api/v3/verifications")
    suspend fun createVerification(
        @Body body: Map<String, String>
    ): NewVerificationResponse
}