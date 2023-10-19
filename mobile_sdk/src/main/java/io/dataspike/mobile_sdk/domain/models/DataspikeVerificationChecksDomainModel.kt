package io.dataspike.mobile_sdk.domain.models

internal data class DataspikeVerificationChecksDomainModel(
    val faceComparison: DataspikeCheckDomainModel,
    val liveness: DataspikeCheckDomainModel,
    val documentMrz: DataspikeCheckDomainModel,
    val poa: DataspikeCheckDomainModel,
)