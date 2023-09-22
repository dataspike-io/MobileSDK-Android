package io.dataspike.mobile_sdk

private var verificationCompletedCallback: VerificationCompletedCallback? = null

fun setVerificationCompletedCallback(
    callback: VerificationCompletedCallback
) {
    verificationCompletedCallback = callback
}

fun passVerificationCompletedResult(verificationSucceeded: Boolean) {
    verificationCompletedCallback?.onVerificationCompleted(verificationSucceeded)
}

interface VerificationCompletedCallback {

    fun onVerificationCompleted(verificationSucceeded: Boolean)
}