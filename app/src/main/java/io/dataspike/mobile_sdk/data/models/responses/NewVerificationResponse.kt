package io.dataspike.mobile_sdk.data.models.responses

import com.google.gson.annotations.SerializedName

internal data class NewVerificationResponse(
    @SerializedName("id")
    val id: String?,
    @SerializedName("status")
    val status: String?,
    @SerializedName("checks")
    val checks: VerificationChecksResponse?,
    @SerializedName("created_at")
    val createdAt: String?,
    @SerializedName("is_sandbox")
    val isSandbox: Boolean?,
    @SerializedName("verification_url")
    val verificationUrl: String?,
    @SerializedName("verification_url_id")
    val verificationUrlId: String?,
    @SerializedName("expires_at")
    val expiresAt: String?,
)