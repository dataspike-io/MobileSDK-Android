package io.dataspike.mobile_sdk.data.models.responses

import com.google.gson.annotations.SerializedName

internal data class NewVerificationResponse(
    @SerializedName("id")
    val id: String?,
    @SerializedName("status")
    val status: String?,
    @SerializedName("checks")
    val checks: VerificationChecksResponse?,
//    @SerializedName("applicant")
//    val applicant: String?,
//    @SerializedName("documents")
//    val documents: List<DocumentResponse>?,
    @SerializedName("created_at")
    val createdAt: String?,
    @SerializedName("is_sandbox")
    val isSandbox: Boolean?,
//    @SerializedName("updated_at")
//    val updatedAt: String?,
    @SerializedName("verification_url")
    val verificationUrl: String?,
    @SerializedName("verification_url_id")
    val verificationUrlId: String?,
//    @SerializedName("country_code")
//    val countryCode: String?,
    @SerializedName("expires_at")
    val expiresAt: String?,
)