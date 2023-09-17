package io.dataspike.mobile_sdk.data.models.responses

import com.google.gson.annotations.SerializedName

data class NewVerificationErrorResponse(
    @SerializedName("validation_errors")
    val validationErrors: List<String>?,
    @SerializedName("error")
    val error: String?,
)