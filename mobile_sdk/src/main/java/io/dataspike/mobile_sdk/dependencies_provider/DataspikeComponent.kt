package io.dataspike.mobile_sdk.dependencies_provider

import io.dataspike.mobile_sdk.data.repository.IDataspikeRepository
import io.dataspike.mobile_sdk.dependencies_provider.model.DataspikeDependencies
import io.dataspike.mobile_sdk.domain.VerificationChecksManager

internal interface DataspikeComponent {

    val dataspikeRepository: IDataspikeRepository
    val verificationSettingsManager: VerificationChecksManager
    val shortId: String?
}

internal fun DataspikeComponent(dependencies: DataspikeDependencies): DataspikeComponent =
    object : DataspikeComponent, DataspikeModule by DataspikeModule.Impl(dependencies) {
        override val shortId: String?
            get() = dependencies.shortId
    }