package io.dataspike.mobile_sdk.domain.models

import com.google.gson.annotations.SerializedName

internal data class FontDomainModel(
    @SerializedName("font")
    var font: String? = null,
    @SerializedName("text_color")
    var textColor: String? = null,
    @SerializedName("text_size")
    var textSize: Float? = null,
)