package io.dataspike.mobile_sdk.dependencies_provider

import io.dataspike.mobile_sdk.dependencies_provider.model.DataspikeDependencies

internal object DataspikeInjector {

    private var _component: DataspikeComponent? = null
    val component: DataspikeComponent
        get() = requireNotNull(_component) { "You need to initialize DataspikeComponent" }

    fun setComponent(dataspikeDependencies: DataspikeDependencies) {
        _component = DataspikeComponent(dataspikeDependencies)
    }
}