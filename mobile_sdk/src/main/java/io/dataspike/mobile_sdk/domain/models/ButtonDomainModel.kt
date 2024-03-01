package io.dataspike.mobile_sdk.domain.models

import com.google.gson.annotations.SerializedName

internal data class ButtonDomainModel(
    @SerializedName("style")
    var style: ButtonStyleDomainModel? = null,
)