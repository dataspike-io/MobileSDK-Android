package io.dataspike.mobile_sdk.view.custom_views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import io.dataspike.mobile_sdk.databinding.ButtonsLayoutBinding
import io.dataspike.mobile_sdk.utils.setup
import io.dataspike.mobile_sdk.view.UIManager
import io.dataspike.mobile_sdk.view.ui_models.ButtonConfigModel

internal const val LEFT_BUTTON = "left_button"
internal const val RIGHT_BUTTON = "right_button"
internal const val TOP_BUTTON = "top_button"
internal const val BOTTOM_BUTTON = "bottom_button"

internal class ButtonsLayout @JvmOverloads constructor(
    context: Context,
    attrSet: AttributeSet? = null
): ConstraintLayout(context, attrSet) {

    private val viewBinding = ButtonsLayoutBinding.inflate(
        LayoutInflater.from(context),
        this
    )
    private val palette = UIManager.getUiConfig().theme.palette
    private val typography = UIManager.getUiConfig().theme.typography
    private val buttonStyle = UIManager.getUiConfig().components.button.style

    fun setup(
        leftButtonConfig: ButtonConfigModel? = null,
        rightButtonConfig: ButtonConfigModel? = null,
        topButtonConfig: ButtonConfigModel? = null,
        bottomButtonConfig: ButtonConfigModel? = null,
        requirementsIsVisible: Boolean = false,
        requirementsAction: (() -> Unit)? = null,
        ) {
        with(viewBinding) {
            with(tvRequirements) {
                isVisible = requirementsIsVisible

                if (isVisible) {
                    setup(
                        font = typography.bodyOne.font,
                        textSize = typography.bodyOne.textSize,
                        textColor = palette.mainColor,
                    )
                    setOnClickListener { requirementsAction?.invoke() }
                }
            }

            leftButtonConfig?.let { config ->
                bLeft.setup(
                    isVisible = config.isVisible,
                    isEnabled = config.isEnabled,
                    isTransparent = config.isTransparent,
                    font = typography.bodyOne.font,
                    textSize = typography.bodyOne.textSize,
                    textColors = config.textColors,
                    text = config.text,
                    backgroundColors = config.backgroundColors,
                    margin = buttonStyle.margin,
                    cornerRadius = buttonStyle.cornerRadius,
                    iconRes = config.iconRes,
                    action = config.action,
                )
            }
            rightButtonConfig?.let { config ->
                bRight.setup(
                    isVisible = config.isVisible,
                    isEnabled = config.isEnabled,
                    isTransparent = config.isTransparent,
                    font = typography.bodyOne.font,
                    textSize = typography.bodyOne.textSize,
                    textColors = config.textColors,
                    text = config.text,
                    backgroundColors = config.backgroundColors,
                    margin = buttonStyle.margin,
                    cornerRadius = buttonStyle.cornerRadius,
                    iconRes = config.iconRes,
                    action = config.action,
                )
            }
            topButtonConfig?.let { config ->
                bTop.setup(
                    isVisible = config.isVisible,
                    isEnabled = config.isEnabled,
                    isTransparent = config.isTransparent,
                    font = typography.bodyOne.font,
                    textSize = typography.bodyOne.textSize,
                    textColors = config.textColors,
                    text = config.text,
                    backgroundColors = config.backgroundColors,
                    margin = buttonStyle.margin,
                    cornerRadius = buttonStyle.cornerRadius,
                    iconRes = config.iconRes,
                    action = config.action,
                )
            }
            bottomButtonConfig?.let { config ->
                bBottom.setup(
                    isVisible = config.isVisible,
                    isEnabled = config.isEnabled,
                    isTransparent = config.isTransparent,
                    font = typography.bodyOne.font,
                    textSize = typography.bodyOne.textSize,
                    textColors = config.textColors,
                    text = config.text,
                    backgroundColors = config.backgroundColors,
                    margin = buttonStyle.margin,
                    cornerRadius = buttonStyle.cornerRadius,
                    iconRes = config.iconRes,
                    action = config.action,
                )
            }
            clHorizontalButtons.isVisible = leftButtonConfig != null && rightButtonConfig != null
        }
    }

    internal fun setButtonEnabled(button: String, isEnabled: Boolean) {
        with(viewBinding) {
            when (button) {
                LEFT_BUTTON -> bLeft.isEnabled = isEnabled

                RIGHT_BUTTON -> bRight.isEnabled = isEnabled

                TOP_BUTTON -> bTop.isEnabled = isEnabled

                BOTTOM_BUTTON -> bBottom.isEnabled = isEnabled
            }
        }
    }
}