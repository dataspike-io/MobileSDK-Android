package io.dataspike.mobile_sdk.data.remote

internal object AppInfo {

    var appName = "NA"
    var appVersion = "NA"

    fun setAppInfo(appName: String, appVersion: String) {
        this.appName = appName
        this.appVersion = appVersion
    }
}