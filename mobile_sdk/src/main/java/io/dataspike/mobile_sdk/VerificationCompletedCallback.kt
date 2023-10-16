package io.dataspike.mobile_sdk

private var verificationCompletedCallback: VerificationCompletedCallback? = null

internal fun setVerificationCompletedCallback(
    callback: VerificationCompletedCallback
) {
    verificationCompletedCallback = callback
}

internal fun passVerificationCompletedResult(
    dataspikeVerificationStatus: DataspikeVerificationStatus
) {
    verificationCompletedCallback?.onVerificationCompleted(dataspikeVerificationStatus)
    verificationCompletedCallback = null
}

interface VerificationCompletedCallback {

    fun onVerificationCompleted(dataspikeVerificationStatus: DataspikeVerificationStatus)
}