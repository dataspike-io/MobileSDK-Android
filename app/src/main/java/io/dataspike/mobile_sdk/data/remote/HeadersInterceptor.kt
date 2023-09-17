package io.dataspike.mobile_sdk.data.remote

import okhttp3.Interceptor
import okhttp3.Response

private const val DS_API_TOKEN = "ds-api-token"
private const val CONTENT_TYPE_HEADER = "Content-Type"
private const val CONTENT_TYPE_VALUE = "application/json"

internal class HeadersInterceptor(
    private val dsApiToken: String,
): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {

        val newRequest = chain.request().newBuilder()
            .header(DS_API_TOKEN, dsApiToken)
            .header(CONTENT_TYPE_HEADER, CONTENT_TYPE_VALUE)
            .build()

        return try {
            chain.proceed(newRequest)
        } catch (e: Exception) {
            e.printStackTrace()
            chain.proceed(chain.request())
        }
    }
}