package io.dataspike.mobile_sdk.view.view_models

import android.os.CountDownTimer
import io.dataspike.mobile_sdk.dependencies_provider.DataspikeInjector
import io.dataspike.mobile_sdk.view.ui_models.TimerUiModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

internal const val ACTIVE = "active"
internal const val COMPLETED = "completed"
internal const val EXPIRED = "expired"
private const val TICK_INTERVAL = 1000L

internal class OnboardingViewModel: BaseViewModel() {

    private val _timerFlow = MutableSharedFlow<TimerUiModel>(replay = 1)
    val timerFlow: SharedFlow<TimerUiModel> = _timerFlow

    fun setVerificationTimer() {
        val verificationManager = DataspikeInjector.component.verificationManager
        val verificationStatus = verificationManager.status
        val millisecondsUntilVerificationExpired =
            verificationManager.millisecondsUntilVerificationExpired

        when {
            verificationStatus == ACTIVE && millisecondsUntilVerificationExpired > 0 -> {
                val timer = object : CountDownTimer(
                    millisecondsUntilVerificationExpired,
                    TICK_INTERVAL
                ) {
                    override fun onTick(millisUntilFinished: Long) {
                        val seconds = millisUntilFinished / 1000
                        val hours = seconds / 3600
                        val minutes = (seconds % 3600) / 60
                        val remainingSeconds = seconds % 60
                        val formattedTime =
                            String.format("%02d:%02d:%02d", hours, minutes, remainingSeconds)

                        sendTimerFlow(formattedTime, ACTIVE)
                    }

                    override fun onFinish() {
                        sendTimerFlow("00:00:00", EXPIRED)
                    }
                }

                timer.start()
            }

            verificationStatus == COMPLETED -> {
                sendTimerFlow("00:00:00", COMPLETED)
            }

            else -> {
                sendTimerFlow("00:00:00", EXPIRED)
            }
        }
    }

    private fun sendTimerFlow(timerText: String, timerStatus: String) {
        launchInVMScope {
            _timerFlow.emit(
                TimerUiModel(
                    timerText,
                    timerStatus,
                )
            )
        }
    }
}