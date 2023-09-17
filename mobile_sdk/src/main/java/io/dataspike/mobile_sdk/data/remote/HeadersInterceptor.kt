package io.dataspike.mobile_sdk.data.remote

import okhttp3.Interceptor
import okhttp3.Response

private const val DS_API_TOKEN = "ds-api-token"
private const val CONTENT_TYPE_HEADER = "Content-Type"
private const val APPLICATION_JSON = "application/json"
private const val MULTIPART_FORM_DATA = "multipart/form-data"

internal class HeadersInterceptor(
    private val dsApiToken: String,
): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {

        val request = chain.request()

        val newRequest = request.newBuilder()
            .header(DS_API_TOKEN, dsApiToken)
            .header(CONTENT_TYPE_HEADER, APPLICATION_JSON)
            .build()

        return try {
            chain.proceed(newRequest)
        } catch (e: Exception) {
            e.printStackTrace()
            chain.proceed(request)
        }
    }
}