package io.dataspike.mobile_sdk.domain.models

internal data class CheckDomainModel(
    val status: String,
    val errors: List<ErrorDomainModel>,
    val pendingDocuments: List<String>,
)