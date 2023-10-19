package io.dataspike.mobile_sdk.data.models.responses

import com.google.gson.annotations.SerializedName

internal data class DataspikeErrorResponse(
    @SerializedName("code")
    val code: Int?,
    @SerializedName("message")
    val message: String?,
)