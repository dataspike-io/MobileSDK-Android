package io.dataspike.mobile_sdk.view.ui_models

internal data class ComponentsModel(
    var button: ButtonModel,
) {

    companion object {
        val defaultComponentsConfig = ComponentsModel(
            button = ButtonModel.defaultButtonConfig,
        )
    }
}