package io.dataspike.mobile_sdk.view.ui_models

internal data class MessagesModel(
    var verificationSuccessful: String,
    var verificationExpired: String,
    var verificationFailed: String,
) {

    companion object {
        val defaultMessages = MessagesModel(
            verificationSuccessful = "You have successfully uploaded all required documents",
            verificationExpired = "Your verification link has expired",
            verificationFailed = "Something went wrong this time",
        )
    }
}