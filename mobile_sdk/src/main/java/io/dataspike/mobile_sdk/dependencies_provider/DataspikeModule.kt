package io.dataspike.mobile_sdk.dependencies_provider

import com.google.gson.Gson
import io.dataspike.mobile_sdk.DataspikeDependencies
import io.dataspike.mobile_sdk.data.api.IDataspikeApiService
import io.dataspike.mobile_sdk.data.image_caching.ImageCacheManager
import io.dataspike.mobile_sdk.data.remote.DataspikeHeadersInterceptor
import io.dataspike.mobile_sdk.data.repository.DataspikeRepositoryImpl
import io.dataspike.mobile_sdk.data.repository.IDataspikeRepository
import io.dataspike.mobile_sdk.domain.VerificationManager
import io.dataspike.mobile_sdk.domain.mappers.CountriesResponseMapper
import io.dataspike.mobile_sdk.domain.mappers.EmptyResponseMapper
import io.dataspike.mobile_sdk.domain.mappers.ProceedWithVerificationResponseMapper
import io.dataspike.mobile_sdk.domain.mappers.UploadImageResponseMapper
import io.dataspike.mobile_sdk.domain.mappers.VerificationResponseMapper
import io.dataspike.mobile_sdk.view.mappers.ProceedWithVerificationUiMapper
import io.dataspike.mobile_sdk.view.mappers.UploadImageUiMapper
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val PROM_BASE_URL = "https://api.dataspike.io/"
private const val DEBUG_BASE_URL = "https://sandboxapi.dataspike.io/"

internal interface DataspikeModule {

    val dataspikeRepository: IDataspikeRepository
    val verificationManager: VerificationManager
    val imageCacheManager: ImageCacheManager
    val shortId: String
    val uploadImageUiMapper: UploadImageUiMapper
    val proceedWithVerificationUiMapper: ProceedWithVerificationUiMapper

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
        private val dataspikeApiService: IDataspikeApiService =
            retrofit.create(IDataspikeApiService::class.java)
        override val shortId: String = dependencies.shortId
        override val dataspikeRepository: IDataspikeRepository
            get() = DataspikeRepositoryImpl(
                dataspikeApiService = dataspikeApiService,
                shortId = shortId,
                verificationResponseMapper = VerificationResponseMapper(),
                uploadImageResponseMapper = UploadImageResponseMapper(),
                countriesResponseMapper = CountriesResponseMapper(),
                emptyResponseMapper = EmptyResponseMapper(),
                proceedWithVerificationResponseMapper = ProceedWithVerificationResponseMapper(),
            )
        override val imageCacheManager: ImageCacheManager = ImageCacheManager()
        override val verificationManager: VerificationManager = VerificationManager()
        override val uploadImageUiMapper: UploadImageUiMapper = UploadImageUiMapper()
        override val proceedWithVerificationUiMapper: ProceedWithVerificationUiMapper =
            ProceedWithVerificationUiMapper()
    }
}