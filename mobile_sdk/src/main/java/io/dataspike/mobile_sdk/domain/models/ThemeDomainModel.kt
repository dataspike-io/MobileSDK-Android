package io.dataspike.mobile_sdk.domain.models

import com.google.gson.annotations.SerializedName

internal data class ThemeDomainModel(
    @SerializedName("palette")
    var palette: PaletteDomainModel? = null,
    @SerializedName("typography")
    var typography: TypographyDomainModel? = null,
)