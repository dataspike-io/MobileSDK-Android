package io.dataspike.mobile_sdk.dependencies_provider

import io.dataspike.mobile_sdk.dependencies_provider.model.SampleAppDependencies

internal object SampleAppInjector {

    private var _component: SampleAppComponent? = null
    val component: SampleAppComponent
        get() = requireNotNull(_component) { "You need to initialize DataspikeComponent" }

    fun setComponent(sampleAppDependencies: SampleAppDependencies) {
        _component = SampleAppComponent(sampleAppDependencies)
    }
}