package io.dataspike.mobile_sdk.data.models.responses

import com.google.gson.annotations.SerializedName

internal data class DataspikeHttpErrorResponse(
    @SerializedName("details")
    val details: String?,
    @SerializedName("error")
    val error: String?,
)