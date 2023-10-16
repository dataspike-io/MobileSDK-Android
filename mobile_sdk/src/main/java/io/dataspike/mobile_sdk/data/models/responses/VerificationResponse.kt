package io.dataspike.mobile_sdk.data.models.responses

import com.google.gson.annotations.SerializedName

internal data class VerificationResponse(
    @SerializedName("id")
    val id: String?,
    @SerializedName("status")
    val status: String?,
    @SerializedName("checks")
    val checks: VerificationChecksResponse?,
    @SerializedName("verification_url")
    val verificationUrl: String?,
    @SerializedName("country_code")
    val countryCode: String?,
    @SerializedName("settings")
    val settings: VerificationSettingsResponse?,
    @SerializedName("expires_at")
    val expiresAt: String?,
)