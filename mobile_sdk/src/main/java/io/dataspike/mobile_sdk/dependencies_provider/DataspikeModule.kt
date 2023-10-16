package io.dataspike.mobile_sdk.dependencies_provider

import com.google.gson.Gson
import io.dataspike.mobile_sdk.data.api.IDataspikeApiService
import io.dataspike.mobile_sdk.data.remote.DataspikeHeadersInterceptor
import io.dataspike.mobile_sdk.data.repository.DataspikeRepositoryImpl
import io.dataspike.mobile_sdk.data.repository.IDataspikeRepository
import io.dataspike.mobile_sdk.dependencies_provider.model.DataspikeDependencies
import io.dataspike.mobile_sdk.domain.VerificationManager
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

internal interface DataspikeModule {

    val dataspikeRepository: IDataspikeRepository

    val verificationSettingsManager: VerificationManager

    class Impl(dependencies: DataspikeDependencies) : DataspikeModule {
        private val okHttpClient: OkHttpClient =
            OkHttpClient().newBuilder()
                .addInterceptor(DataspikeHeadersInterceptor(dependencies.dsApiToken))
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

        private val dataspikeApiService: IDataspikeApiService
            get() = retrofit.create(IDataspikeApiService::class.java)

        override val dataspikeRepository: IDataspikeRepository
            get() = DataspikeRepositoryImpl(
                dataspikeApiService = dataspikeApiService,
            )

        //TODO change to class
        override val verificationSettingsManager: VerificationManager =
            VerificationManager
    }

    companion object {
        private const val PROM_BASE_URL = "https://api.dataspike.io/"
        private const val DEBUG_BASE_URL = "https://sandboxapi.dataspike.io/"
    }
}