package io.dataspike.mobile_sdk.view.ui_models

import android.graphics.Color
import io.dataspike.mobile_sdk.utils.parseColorString

internal data class FontModel(
    var font: String,
    var textColor: Int,
    var textSize: Float,
) {

    companion object {
        val defaultLightHeaderConfig = FontModel(
            font = "mont_bold",
            textColor = parseColorString("#000000") ?: Color.WHITE,
            textSize = 24F,
        )
        val defaultDarkHeaderConfig = FontModel(
            font = "mont_bold",
            textColor = parseColorString("#FFFFFF") ?: Color.WHITE,
            textSize = 24F,
        )
        val defaultLightBodyOneConfig = FontModel(
            font = "mont_regular",
            textColor = parseColorString("#000000") ?: Color.WHITE,
            textSize = 16F,
        )
        val defaultDarkBodyOneConfig = FontModel(
            font = "mont_regular",
            textColor = parseColorString("#FFFFFF") ?: Color.WHITE,
            textSize = 16F,
        )
        val defaultLightBodyTwoConfig = FontModel(
            font = "mont_regular",
            textColor = parseColorString("#50000000") ?: Color.WHITE,
            textSize = 14F,
        )
        val defaultDarkBodyTwoConfig = FontModel(
            font = "mont_regular",
            textColor = parseColorString("#50FFFFFF") ?: Color.WHITE,
            textSize = 14F,
        )
    }
}