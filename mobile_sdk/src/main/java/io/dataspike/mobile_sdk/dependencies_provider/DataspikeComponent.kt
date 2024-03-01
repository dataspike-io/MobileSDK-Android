package io.dataspike.mobile_sdk.dependencies_provider

import io.dataspike.mobile_sdk.DataspikeDependencies
import io.dataspike.mobile_sdk.data.image_caching.ImageCacheManager
import io.dataspike.mobile_sdk.data.repository.IDataspikeRepository
import io.dataspike.mobile_sdk.domain.VerificationManager
import io.dataspike.mobile_sdk.view.mappers.ProceedWithVerificationUiMapper
import io.dataspike.mobile_sdk.view.mappers.UploadImageUiMapper

internal interface DataspikeComponent {

    val shortId: String
    val dataspikeRepository: IDataspikeRepository
    val verificationManager: VerificationManager
    val imageCacheManager: ImageCacheManager
    val uploadImageUiMapper: UploadImageUiMapper
    val proceedWithVerificationUiMapper: ProceedWithVerificationUiMapper
}

internal fun DataspikeComponent(dependencies: DataspikeDependencies): DataspikeComponent {
    return object : DataspikeComponent, DataspikeModule by DataspikeModule.Impl(dependencies) {}
}