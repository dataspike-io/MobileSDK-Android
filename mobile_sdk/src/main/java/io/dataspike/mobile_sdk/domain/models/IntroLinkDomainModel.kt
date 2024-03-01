package io.dataspike.mobile_sdk.domain.models

import com.google.gson.annotations.SerializedName

internal data class IntroLinkDomainModel(
    @SerializedName("poi")
    var poi: String? = null,
    @SerializedName("poa")
    var poa: String? = null,
)