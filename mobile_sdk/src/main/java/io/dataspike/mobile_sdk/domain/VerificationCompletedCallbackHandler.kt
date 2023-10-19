package io.dataspike.mobile_sdk.domain

import io.dataspike.mobile_sdk.DataspikeVerificationStatus
import io.dataspike.mobile_sdk.VerificationCompletedCallback

private var verificationCompletedCallback: VerificationCompletedCallback? = null
private var verificationStatus: DataspikeVerificationStatus? = null
internal fun setVerificationResult(
    dataspikeVerificationStatus: DataspikeVerificationStatus
) {
    verificationStatus = dataspikeVerificationStatus
}

internal fun setVerificationCompletedCallback(
    callback: VerificationCompletedCallback
) {
    verificationCompletedCallback = callback
}

internal fun passVerificationCompletedResult() {
    verificationCompletedCallback?.onVerificationCompleted(verificationStatus)
    verificationCompletedCallback = null
}