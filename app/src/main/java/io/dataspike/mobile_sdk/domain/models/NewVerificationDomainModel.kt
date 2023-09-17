package io.dataspike.mobile_sdk.domain.models

internal data class NewVerificationDomainModel(
    val id: String,
    val status: String,
    val checks: VerificationChecksDomainModel,
    val createdAt: String,
    val isSandbox: Boolean,
    val verificationUrl: String,
    val verificationUrlId: String,
    val expiresAt: String,
)