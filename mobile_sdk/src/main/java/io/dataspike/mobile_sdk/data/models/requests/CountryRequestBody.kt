package io.dataspike.mobile_sdk.data.models.requests

import com.google.gson.annotations.SerializedName

internal data class CountryRequestBody(
    @SerializedName("country")
    val country: String,
)