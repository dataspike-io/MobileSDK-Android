package io.dataspike.mobile_sdk.data.models.responses

import com.google.gson.annotations.SerializedName

internal data class InternalServerErrorResponse(
    @SerializedName("message")
    val message: String?,
)