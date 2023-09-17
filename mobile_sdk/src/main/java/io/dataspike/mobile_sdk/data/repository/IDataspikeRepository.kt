package io.dataspike.mobile_sdk.data.repository

import io.dataspike.mobile_sdk.domain.models.EmptyState
import io.dataspike.mobile_sdk.domain.models.UploadImageState
import io.dataspike.mobile_sdk.domain.models.VerificationState
import java.io.File

internal interface IDataspikeRepository {

    suspend fun getVerification(
        shortId: String,
    ): VerificationState

    suspend fun uploadImage(
        shortId: String,
        documentType: String,
        image: File,
    ): UploadImageState

    suspend fun proceedWithVerification(
        shortId: String,
        body: Map<String, String>,
    ): EmptyState
}