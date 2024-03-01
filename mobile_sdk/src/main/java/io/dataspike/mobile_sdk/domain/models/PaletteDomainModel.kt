package io.dataspike.mobile_sdk.domain.models

import com.google.gson.annotations.SerializedName

internal data class PaletteDomainModel(
    @SerializedName("background_color")
    var backgroundColor: String? = null,
    @SerializedName("main_color")
    var mainColor: String? = null,
    @SerializedName("success_color")
    var successColor: String? = null,
    @SerializedName("error_color")
    val errorColor: String? = null,
)