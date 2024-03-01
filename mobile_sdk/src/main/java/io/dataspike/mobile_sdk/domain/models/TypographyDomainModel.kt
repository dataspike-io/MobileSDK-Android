package io.dataspike.mobile_sdk.domain.models

import com.google.gson.annotations.SerializedName

internal data class TypographyDomainModel(
    @SerializedName("header")
    var header: FontDomainModel? = null,
    @SerializedName("body_one")
    var bodyOne: FontDomainModel? = null,
    @SerializedName("body_two")
    var bodyTwo: FontDomainModel? = null,
)