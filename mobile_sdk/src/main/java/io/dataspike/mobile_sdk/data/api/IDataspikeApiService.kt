package io.dataspike.mobile_sdk.data.api

import io.dataspike.mobile_sdk.data.models.requests.CountryRequestBody
import io.dataspike.mobile_sdk.data.models.responses.CountryResponse
import io.dataspike.mobile_sdk.data.models.responses.EmptyResponse
import io.dataspike.mobile_sdk.data.models.responses.ProceedWithVerificationResponse
import io.dataspike.mobile_sdk.data.models.responses.UploadImageResponse
import io.dataspike.mobile_sdk.data.models.responses.VerificationResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

internal interface IDataspikeApiService {

    @GET("api/v3/sdk/{short_id}")
    suspend fun getVerification(
        @Path("short_id") shortId: String,
    ): VerificationResponse

    @Multipart
    @POST("api/v3/upload/sdk/{short_id}")
    suspend fun uploadImage(
        @Path("short_id") shortId: String,
        @Part("document_type") documentType: RequestBody,
        @Part file: MultipartBody.Part?,
    ): UploadImageResponse

    @POST("api/v3/sdk/{short_id}/set_country")
    suspend fun setCountry(
        @Path("short_id") shortId: String,
        @Body body: CountryRequestBody,
    ): EmptyResponse

    @GET("api/v3/public/dictionary/countries")
    suspend fun getCountries(): Array<CountryResponse>

    @POST("api/v3/sdk/{short_id}/proceed")
    suspend fun proceedWithVerification(
        @Path("short_id") shortId: String,
    ): ProceedWithVerificationResponse
}