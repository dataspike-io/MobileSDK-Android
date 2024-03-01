package io.dataspike.mobile_sdk.view.ui_models

import androidx.annotation.StringRes

internal data class OnboardingUiModel(
    val imageLink: String,
    @StringRes val stringResId: Int,
    val pageType: String,
)