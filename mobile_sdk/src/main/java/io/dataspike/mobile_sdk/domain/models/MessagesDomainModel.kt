package io.dataspike.mobile_sdk.domain.models

import com.google.gson.annotations.SerializedName

internal data class MessagesDomainModel(
    @SerializedName("verification_successful")
    var verificationSuccessful: String? = null,
    @SerializedName("verification_expired")
    var verificationExpired: String? = null,
    @SerializedName("verification_failed")
    var verificationFailed: String? = null,
)