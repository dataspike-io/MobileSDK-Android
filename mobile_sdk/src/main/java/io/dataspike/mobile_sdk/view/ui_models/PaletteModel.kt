package io.dataspike.mobile_sdk.view.ui_models

import android.graphics.Color
import io.dataspike.mobile_sdk.utils.lightenColor
import io.dataspike.mobile_sdk.utils.parseColorString
import io.dataspike.mobile_sdk.view.LIGHTER_FRACTION
import io.dataspike.mobile_sdk.view.LIGHT_FRACTION

internal data class PaletteModel(
    var backgroundColor: Int,
    var mainColor: Int,
    var lightMainColor: Int,
    var lighterMainColor: Int,
    var successColor: Int,
    var lighterSuccessColor: Int,
    val errorColor: Int,
    val lighterErrorColor: Int,
) {

    companion object {
        private val mainColor = parseColorString("#6764E8") ?: Color.WHITE
        private val successColor = parseColorString("#52C27F") ?: Color.WHITE
        private val errorColor = parseColorString("#FF5387") ?: Color.WHITE
        val defaultLightThemePalette = PaletteModel(
            backgroundColor = parseColorString("#FFFFFF") ?: Color.WHITE,
            mainColor = mainColor,
            lightMainColor = lightenColor(mainColor, LIGHT_FRACTION)  ?: Color.WHITE,
            lighterMainColor = lightenColor(mainColor, LIGHTER_FRACTION) ?: Color.WHITE,
            successColor = successColor,
            lighterSuccessColor = lightenColor(successColor, LIGHTER_FRACTION) ?: Color.WHITE,
            errorColor = errorColor,
            lighterErrorColor = lightenColor(errorColor, LIGHTER_FRACTION) ?: Color.WHITE,
        )
        val defaultDarkThemePalette = PaletteModel(
            backgroundColor = parseColorString("#1F1F1F") ?: Color.WHITE,
            mainColor = mainColor,
            lightMainColor = lightenColor(mainColor, LIGHT_FRACTION) ?: Color.WHITE,
            lighterMainColor = lightenColor(mainColor, LIGHTER_FRACTION) ?: Color.WHITE,
            successColor = successColor,
            lighterSuccessColor = lightenColor(successColor, LIGHTER_FRACTION) ?: Color.WHITE,
            errorColor = errorColor,
            lighterErrorColor = lightenColor(errorColor, LIGHTER_FRACTION) ?: Color.WHITE,
        )
    }
}