package io.dataspike.mobile_sdk.data.models.responses

import com.google.gson.annotations.SerializedName

internal data class CountryResponse(
    @SerializedName("alpha_2")
    val alphaTwo: String?,
    @SerializedName("alpha_3")
    val alphaThree: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("continent")
    val continent: String?,
)