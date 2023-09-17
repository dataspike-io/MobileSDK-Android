package io.dataspike.mobile_sdk.data.models.responses

import com.google.gson.annotations.SerializedName

data class ErrorResponse(
    @SerializedName("code")
    val code: Int?,
    @SerializedName("message")
    val message: String?,
)