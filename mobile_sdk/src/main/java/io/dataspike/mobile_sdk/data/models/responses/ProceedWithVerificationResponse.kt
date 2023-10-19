package io.dataspike.mobile_sdk.data.models.responses

import com.google.gson.annotations.SerializedName

internal data class ProceedWithVerificationResponse(
    @SerializedName("id")
    val id: String?,
    @SerializedName("status")
    val status: String?,
)
