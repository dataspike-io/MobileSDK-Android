package io.dataspike.mobile_sdk.dependencies_provider

import com.google.gson.Gson
import io.dataspike.mobile_sdk.data.api.ISampleAppApiService
import io.dataspike.mobile_sdk.data.remote.HeadersInterceptor
import io.dataspike.mobile_sdk.data.repository.ISampleAppRepository
import io.dataspike.mobile_sdk.data.repository.SampleAppRepositoryImpl
import io.dataspike.mobile_sdk.dependencies_provider.model.SampleAppDependencies
import io.dataspike.mobile_sdk.domain.mappers.NewVerificationResponseMapper
import io.dataspike.mobile_sdk.domain.mappers.NewVerificationUiMapper
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val PROM_BASE_URL = "https://api.dataspike.io/"
private const val DEBUG_BASE_URL = "https://sandboxapi.dataspike.io/"

internal interface SampleAppModule {

    val sampleAppRepository: ISampleAppRepository
    val newVerificationUiMapper: NewVerificationUiMapper

    class Impl(dependencies: SampleAppDependencies) : SampleAppModule {

        private val okHttpClient: OkHttpClient =
            OkHttpClient().newBuilder()
                .addInterceptor(HeadersInterceptor(dependencies.dsApiToken))
                .addInterceptor(
                    HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
                )
                .build()

        private val baseUrl = if (dependencies.isDebug) {
            DEBUG_BASE_URL
        } else {
            PROM_BASE_URL
        }
        private val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .build()
        private val sampleAppApiService: ISampleAppApiService =
            retrofit.create(ISampleAppApiService::class.java)
        override val sampleAppRepository: ISampleAppRepository =
            SampleAppRepositoryImpl(
                sampleAppApiService = sampleAppApiService,
                newVerificationResponseMapper = NewVerificationResponseMapper(),
            )
        override val newVerificationUiMapper: NewVerificationUiMapper = NewVerificationUiMapper()
    }
}