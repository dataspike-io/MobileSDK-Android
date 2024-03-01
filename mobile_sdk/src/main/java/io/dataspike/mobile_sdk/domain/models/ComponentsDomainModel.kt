package io.dataspike.mobile_sdk.domain.models

import com.google.gson.annotations.SerializedName

internal data class ComponentsDomainModel(
    @SerializedName("button")
    var button: ButtonDomainModel? = null,
)