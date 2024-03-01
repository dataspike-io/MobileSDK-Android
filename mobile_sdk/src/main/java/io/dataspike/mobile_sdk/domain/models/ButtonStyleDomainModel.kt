package io.dataspike.mobile_sdk.domain.models

import com.google.gson.annotations.SerializedName

internal data class ButtonStyleDomainModel(
    @SerializedName("margin")
    var margin: Float? = null,
    @SerializedName("corner_radius")
    var cornerRadius: Float? = null,
)
