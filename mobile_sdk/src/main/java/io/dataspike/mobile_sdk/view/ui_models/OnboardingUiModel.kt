package io.dataspike.mobile_sdk.view.ui_models

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

internal data class OnboardingUiModel(
    @DrawableRes val drawableResId: Int,
    @StringRes val stringResId: Int,
    val pageType: String,
)