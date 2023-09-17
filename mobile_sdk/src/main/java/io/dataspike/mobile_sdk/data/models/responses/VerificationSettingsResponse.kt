package io.dataspike.mobile_sdk.data.models.responses

import com.google.gson.annotations.SerializedName

internal data class VerificationSettingsResponse(
    @SerializedName("poi_required")
    val poiRequired: Boolean?,
    @SerializedName("poi_allowed_documents")
    val poiAllowedDocuments: List<String>?,
    @SerializedName("face_comparison_required")
    val faceComparisonRequired: Boolean?,
    @SerializedName("face_comparison_allowed_documents")
    val faceComparisonAllowedDocuments: List<String>?,
    @SerializedName("poa_required")
    val poaRequired: Boolean?,
    @SerializedName("poa_allowed_documents")
    val poaAllowedDocuments: List<String>?,
    @SerializedName("countries")
    val countries: List<String>?,
)