package io.dataspike.mobile_sdk.dependencies_provider

import io.dataspike.mobile_sdk.data.repository.ISampleAppRepository
import io.dataspike.mobile_sdk.dependencies_provider.model.SampleAppDependencies
import io.dataspike.mobile_sdk.domain.mappers.NewVerificationUiMapper

internal interface SampleAppComponent {

    val sampleAppRepository: ISampleAppRepository
    val newVerificationUiMapper: NewVerificationUiMapper
}

internal fun SampleAppComponent(dependencies: SampleAppDependencies): SampleAppComponent =
    object : SampleAppComponent, SampleAppModule by SampleAppModule.Impl(dependencies) {}