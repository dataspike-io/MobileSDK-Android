package io.dataspike.mobile_sdk

private var verificationCompletedCallback: VerificationCompletedCallback? = null

internal fun setVerificationCompletedCallback(
    callback: VerificationCompletedCallback
) {
    verificationCompletedCallback = callback
}

internal fun passVerificationCompletedResult(verificationSucceeded: Boolean) {
    verificationCompletedCallback?.onVerificationCompleted(verificationSucceeded)
    verificationCompletedCallback = null
}

interface VerificationCompletedCallback {

    fun onVerificationCompleted(verificationSucceeded: Boolean)
}