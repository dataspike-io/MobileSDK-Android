package io.dataspike.mobile_sdk.view.ui_models

internal data class UiConfigModel(
    var theme: ThemeModel,
    var components: ComponentsModel,
    var messages: MessagesModel,
    var links: LinksModel,
    var options: OptionsModel,
) {

    companion object {
        fun getConfig(darkModeIsEnabled: Boolean): UiConfigModel {
            val theme = if (darkModeIsEnabled) {
                ThemeModel.defaultDarkUITheme
            } else {
                ThemeModel.defaultLightUITheme
            }

            return UiConfigModel(
                theme = theme,
                components = ComponentsModel.defaultComponentsConfig,
                messages = MessagesModel.defaultMessages,
                links = LinksModel.defaultLinks,
                options = OptionsModel.defaultOptions,
            )
        }
    }
}