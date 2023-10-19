package io.dataspike.mobile_sdk.domain.models

internal data class DataspikeCheckDomainModel(
    val status: String,
    val errors: List<DataspikeErrorDomainModel>,
    val pendingDocuments: List<String>,
)