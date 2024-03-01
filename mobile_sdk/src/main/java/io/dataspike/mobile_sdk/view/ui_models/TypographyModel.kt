package io.dataspike.mobile_sdk.view.ui_models

internal data class TypographyModel(
    var header: FontModel,
    var bodyOne: FontModel,
    var bodyTwo: FontModel,
) {

    companion object {
        val defaultLightTypography = TypographyModel(
            header = FontModel.defaultLightHeaderConfig,
            bodyOne = FontModel.defaultLightBodyOneConfig,
            bodyTwo = FontModel.defaultLightBodyTwoConfig,
        )
        val defaultDarkTypography = TypographyModel(
            header = FontModel.defaultDarkHeaderConfig,
            bodyOne = FontModel.defaultDarkBodyOneConfig,
            bodyTwo = FontModel.defaultDarkBodyTwoConfig,
        )
    }
}