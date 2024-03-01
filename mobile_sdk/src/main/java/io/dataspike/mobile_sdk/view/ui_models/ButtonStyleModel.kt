package io.dataspike.mobile_sdk.view.ui_models

internal data class ButtonStyleModel(
    var margin: Float,
    var cornerRadius: Float,
) {

    companion object {
        val defaultButtonStyle = ButtonStyleModel(
            margin = 16F,
            cornerRadius = 16F,
        )
    }
}