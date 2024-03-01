package io.dataspike.mobile_sdk.domain.models

import com.google.gson.annotations.SerializedName

internal data class UiConfigDomainModel(
    @SerializedName("themes")
    var themes: ThemesDomainModel? = null,
    @SerializedName("components")
    var components: ComponentsDomainModel? = null,
    @SerializedName("messages")
    var messages: MessagesDomainModel? = null,
    @SerializedName("links")
    var links: LinksDomainModel? = null,
    @SerializedName("options")
    var options: OptionsDomainModel? = null,
)