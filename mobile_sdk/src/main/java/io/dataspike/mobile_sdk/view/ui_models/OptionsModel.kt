package io.dataspike.mobile_sdk.view.ui_models

internal data class OptionsModel(
    var showTimer: Boolean,
    var showSteps: Boolean,
    var disableDarkMode : Boolean,
) {

    companion object {
        val defaultOptions = OptionsModel(
            showTimer = true,
            showSteps = true,
            disableDarkMode = false,
        )
    }
}