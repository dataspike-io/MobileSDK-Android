package io.dataspike.mobile_sdk.dependencies_provider

import io.dataspike.mobile_sdk.data.repository.ISampleAppRepository
import io.dataspike.mobile_sdk.dependencies_provider.model.SampleAppDependencies

internal interface SampleAppComponent {

    val sampleAppRepository: ISampleAppRepository
}

internal fun SampleAppComponent(dependencies: SampleAppDependencies): SampleAppComponent =
    object : SampleAppComponent, SampleAppModule by SampleAppModule.Impl(dependencies) {}