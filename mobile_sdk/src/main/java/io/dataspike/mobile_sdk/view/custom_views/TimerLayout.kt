package io.dataspike.mobile_sdk.view.custom_views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import io.dataspike.mobile_sdk.R
import io.dataspike.mobile_sdk.databinding.TimerLayoutBinding
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

    fun setup(
        timerState: TimerUiModel,
        completedAction: (() -> Unit)? = null,
        expiredAction: (() -> Unit)? = null,
        ) {
        with(viewBinding) {
            when (timerState.timerStatus) {
                ACTIVE -> {
                    tvTimer.visibility = VISIBLE
                    tvTimer.text = context.getString(
                        R.string.time_left,
                        timerState.timerString
                    )
                }

                COMPLETED -> {
                    completedAction?.invoke()
                }

                else -> {
                    tvTimerExpired.visibility = VISIBLE
                    tvTimerExpired.text = context.getString(
                        R.string.time_left,
                        timerState.timerString
                    )

                    expiredAction?.invoke()
                }
            }
        }
    }
}