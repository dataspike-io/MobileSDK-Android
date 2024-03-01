package io.dataspike.mobile_sdk.view.custom_views

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.updatePadding
import io.dataspike.mobile_sdk.R
import io.dataspike.mobile_sdk.databinding.TimerLayoutBinding
import io.dataspike.mobile_sdk.utils.dpToPx
import io.dataspike.mobile_sdk.utils.setup
import io.dataspike.mobile_sdk.view.UIManager
import io.dataspike.mobile_sdk.view.ui_models.TimerUiModel
import io.dataspike.mobile_sdk.view.view_models.ACTIVE
import io.dataspike.mobile_sdk.view.view_models.COMPLETED

internal class TimerLayout @JvmOverloads constructor(
    context: Context,
    attrSet: AttributeSet? = null
): ConstraintLayout(context, attrSet) {

    private val viewBinding = TimerLayoutBinding.inflate(
        LayoutInflater.from(context),
        this
    )
    private val palette = UIManager.getUiConfig().theme.palette
    private val typography = UIManager.getUiConfig().theme.typography
    private val showTimer = UIManager.getUiConfig().options.showTimer

    private fun setupTimer(
        isExpired: Boolean,
        timerState: TimerUiModel,
    ) {
        with(viewBinding.tvTimer) {
            visibility = VISIBLE
            setup(
                font = typography.bodyTwo.font,
                textSize = typography.bodyTwo.textSize,
                textColor = if (isExpired) palette.errorColor else palette.mainColor,
            )
            text = context.getString(
                R.string.time_left,
                timerState.timerString
            )

            val drawableColor: Int
            val backgroundColor: Int

            if (isExpired) {
                drawableColor = palette.errorColor
                backgroundColor = palette.lighterErrorColor
            } else {
                drawableColor = palette.mainColor
                backgroundColor = palette.lighterMainColor
            }

            val backgroundDrawable = GradientDrawable().apply {
                shape = GradientDrawable.RECTANGLE
                color = ColorStateList.valueOf(backgroundColor)
                cornerRadius = dpToPx(8F)
            }
            val leftDrawable = ResourcesCompat.getDrawable(
                resources,
                R.drawable.timer_clock,
                null
            )

            background = backgroundDrawable
            updatePadding(
                dpToPx(6F).toInt(),
                dpToPx(6F).toInt(),
                dpToPx(6F).toInt(),
                dpToPx(6F).toInt()
            )
            DrawableCompat.setTint(leftDrawable ?: return, drawableColor)
            setCompoundDrawablesWithIntrinsicBounds(leftDrawable, null, null, null)
        }
    }

    fun setup(
        timerState: TimerUiModel,
        completedAction: (() -> Unit)? = null,
        expiredAction: (() -> Unit)? = null,
    ) {
        if (showTimer) {
            when (timerState.timerStatus) {
                ACTIVE -> {
                    setupTimer(false, timerState)
                }

                COMPLETED -> {
                    completedAction?.invoke()
                }

                else -> {
                    setupTimer(true, timerState)
                    expiredAction?.invoke()
                }
            }
        } else {
            visibility = View.GONE
        }
    }
}