package io.dataspike.mobile_sdk.domain.models

internal data class VerificationChecksDomainModel(
    val faceComparison: CheckDomainModel,
    val liveness: CheckDomainModel,
    val documentMrz: CheckDomainModel,
    val poa: CheckDomainModel,
)