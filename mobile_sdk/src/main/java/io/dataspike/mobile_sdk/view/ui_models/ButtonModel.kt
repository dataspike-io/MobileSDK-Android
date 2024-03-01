package io.dataspike.mobile_sdk.view.ui_models

internal data class ButtonModel(
    var style: ButtonStyleModel,
) {

    companion object {
        val defaultButtonConfig = ButtonModel(
            style = ButtonStyleModel.defaultButtonStyle,
        )
    }
}