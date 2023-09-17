package io.dataspike.mobile_sdk.data.models.responses

import com.google.gson.annotations.SerializedName

data class HttpErrorResponse(
    @SerializedName("details")
    val details: String?,
    @SerializedName("error")
    val error: String?,
)