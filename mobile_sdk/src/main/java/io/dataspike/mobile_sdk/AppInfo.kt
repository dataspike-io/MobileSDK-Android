package io.dataspike.mobile_sdk

internal object AppInfo {

    var appName = ""
    var appVersion = ""

    fun setAppInfo(appName: String, appVersion: String) {
        this.appName = appName
        this.appVersion = appVersion
    }
}