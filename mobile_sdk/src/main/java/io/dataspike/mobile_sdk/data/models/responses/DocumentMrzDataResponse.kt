package io.dataspike.mobile_sdk.data.models.responses

import com.google.gson.annotations.SerializedName

internal data class DocumentMrzDataResponse(
    @SerializedName("document_type")
    val documentType: String?,
    @SerializedName("country")
    val country: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("surname")
    val surname: String?,
    @SerializedName("doc_number")
    val docNumber: String?,
    @SerializedName("nationality")
    val nationality: String?,
    @SerializedName("birth_date")
    val birthDate: String?,
    @SerializedName("sex")
    val sex: String?,
    @SerializedName("expiry_date")
    val expiryDate: String?,
)