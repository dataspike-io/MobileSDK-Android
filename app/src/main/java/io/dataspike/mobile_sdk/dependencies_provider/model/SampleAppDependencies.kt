package io.dataspike.mobile_sdk.dependencies_provider.model

internal data class SampleAppDependencies(
    val isDebug: Boolean,
    val dsApiToken: String,
    val shortId: String,
) {

    companion object {
        val DEFAULT = SampleAppDependencies(
            isDebug = true,
            // put your api token here
            dsApiToken = "",
            shortId = "",
        )
    }
}