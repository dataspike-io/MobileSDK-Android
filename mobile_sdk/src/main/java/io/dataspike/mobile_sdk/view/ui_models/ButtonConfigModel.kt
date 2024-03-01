package io.dataspike.mobile_sdk.view.ui_models

internal data class ButtonConfigModel(
    val isVisible: Boolean,
    val isEnabled: Boolean,
    val isTransparent: Boolean,
    val backgroundColors: Pair<Int, Int>,
    val text: String,
    val textColors: Pair<Int, Int>,
    val iconRes: Int? = null,
    val action: () -> Unit,
)