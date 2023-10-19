package io.dataspike.mobile_sdk.data.remote

internal object AppInfo {

    var appName = "n/a"
    var appVersion = "n/a"

    fun setAppInfo(appName: String, appVersion: String) {
        this.appName = appName
        this.appVersion = appVersion
    }
}