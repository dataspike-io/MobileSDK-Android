package io.dataspike.mobile_sdk.domain.models

import com.google.gson.annotations.SerializedName

internal data class OptionsDomainModel(
    @SerializedName("show_timer")
    var showTimer: Boolean? = null,
    @SerializedName("show_steps")
    var showSteps: Boolean? = null,
    @SerializedName("disable_dark_mode")
    var disableDarkMode : Boolean? = null,
)