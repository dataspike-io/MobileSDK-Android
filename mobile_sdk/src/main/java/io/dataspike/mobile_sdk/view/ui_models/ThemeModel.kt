package io.dataspike.mobile_sdk.view.ui_models

internal data class ThemeModel(
    var palette: PaletteModel,
    var typography: TypographyModel,
) {

    companion object {
        val defaultLightUITheme = ThemeModel(
            palette = PaletteModel.defaultLightThemePalette,
            typography = TypographyModel.defaultLightTypography,
        )
        val defaultDarkUITheme = ThemeModel(
            palette = PaletteModel.defaultDarkThemePalette,
            typography = TypographyModel.defaultDarkTypography,
        )
    }
}