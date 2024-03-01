package io.dataspike.mobile_sdk.domain.models

import com.google.gson.annotations.SerializedName

internal data class RequirementsLinkDomainModel(
    @SerializedName("poi")
    var poi: String? = null,
    @SerializedName("liveness")
    var liveness: String? = null,
    @SerializedName("poa")
    var poa: String? = null,
)