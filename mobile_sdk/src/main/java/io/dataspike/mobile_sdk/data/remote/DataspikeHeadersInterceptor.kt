package io.dataspike.mobile_sdk.data.remote

import android.os.Build
import io.dataspike.mobile_sdk.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response
import java.util.Locale

private const val USER_AGENT = "user-agent"
private const val DS_API_TOKEN = "ds-api-token"
private const val CONTENT_TYPE_HEADER = "Content-Type"
private const val APPLICATION_JSON = "application/json"

internal class DataspikeHeadersInterceptor(
    private val dsApiToken: String,
): Interceptor {

    private val userAgent = "MobileSDK-Android/${BuildConfig.VERSION_NAME} " +
        "(Android ${Build.VERSION.RELEASE}; ${Build.MANUFACTURER} ${Build.MODEL}) " +
        "${AppInfo.appName}/${AppInfo.appVersion} Locale/${Locale.getDefault()}"

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val newRequest = request.newBuilder()
            .header(DS_API_TOKEN, dsApiToken)
            .header(CONTENT_TYPE_HEADER, APPLICATION_JSON)
            .header(USER_AGENT, userAgent)
            .build()

        return kotlin.runCatching {
            chain.proceed(newRequest)
        }.onFailure { throwable ->
            throwable.printStackTrace()
        }.getOrNull() ?: chain.proceed(request)
    }
}