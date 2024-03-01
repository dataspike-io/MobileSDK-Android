package io.dataspike.mobile_sdk.domain.models

import com.google.gson.annotations.SerializedName

internal data class ThemesDomainModel(
    @SerializedName("light")
    var light: ThemeDomainModel? = null,
    @SerializedName("dark")
    var dark: ThemeDomainModel? = null,
)