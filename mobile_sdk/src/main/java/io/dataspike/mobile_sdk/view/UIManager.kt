package io.dataspike.mobile_sdk.view

import io.dataspike.mobile_sdk.view.ui_models.UiConfigModel

internal const val LIGHT_FRACTION = .5
internal const val LIGHTER_FRACTION = .2
internal const val MONT_REGULAR = "mont_regular"
internal const val MONT_SEMI_BOLD = "mont_semi_bold"
internal const val MONT_BOLD = "mont_bold"
internal const val ROBOTO_REGULAR = "roboto_regular"
internal const val ROBOTO_SEMI_BOLD = "roboto_semi_bold"
internal const val ROBOTO_BOLD = "roboto_bold"

internal object UIManager {

    private lateinit var uiConfig: UiConfigModel

    fun initUIManager(uiConfigModel: UiConfigModel) {
        uiConfig = uiConfigModel
    }

    fun getUiConfig(): UiConfigModel {
        return if (::uiConfig.isInitialized) {
            uiConfig
        } else {
            UiConfigModel.getConfig(false)
        }
    }
}