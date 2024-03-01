package io.dataspike.mobile_sdk.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import com.bumptech.glide.Glide
import io.dataspike.mobile_sdk.DataspikeVerificationStatus
import io.dataspike.mobile_sdk.R
import io.dataspike.mobile_sdk.databinding.FragmentVerificationExpiredBinding
import io.dataspike.mobile_sdk.domain.setVerificationResult
import io.dataspike.mobile_sdk.utils.setup
import io.dataspike.mobile_sdk.view.UIManager
import io.dataspike.mobile_sdk.view.ui_models.ButtonConfigModel
import io.dataspike.mobile_sdk.view.ui_models.TimerUiModel
import io.dataspike.mobile_sdk.view.view_models.EXPIRED

internal class VerificationExpiredFragment : BaseFragment() {

    private var viewBinding: FragmentVerificationExpiredBinding? = null
    private val imageLinks = UIManager.getUiConfig().links.verificationResult
    private val expiredMessage = UIManager.getUiConfig().messages.verificationExpired

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentVerificationExpiredBinding.inflate(
            inflater,
            container,
            false
        )

        return viewBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setVerificationResult(DataspikeVerificationStatus.VERIFICATION_EXPIRED)

        with(viewBinding?: return) {
            tvVerificationExpiredTitle.setup(
                font = typography.header.font,
                textSize = typography.header.textSize,
                textColor = typography.header.textColor,
            )
            tlTimer.setup(
                timerState = TimerUiModel(
                    timerString = requireContext().getString(R.string._00_00_00),
                    timerStatus = EXPIRED,
                )
            )
            Glide
                .with(ivClock)
                .load(imageLinks.verificationExpired)
                .into(ivClock)
            with(tvVerificationExpiredDescription) {
                setup(
                    font = typography.bodyTwo.font,
                    textSize = typography.bodyTwo.textSize,
                    textColor = typography.bodyTwo.textColor,
                )
                text = expiredMessage
            }
            bButtons.setup(
                topButtonConfig = ButtonConfigModel(
                    isVisible = true,
                    isEnabled = true,
                    isTransparent = false,
                    backgroundColors = Pair(palette.mainColor, palette.lightMainColor),
                    text = getString(R.string.complete),
                    textColors = Pair(palette.backgroundColor, palette.backgroundColor),
                    action = ::passVerificationStatusAndFinish,
                ),
            )
        }

        val onBackPressedCallback = object : OnBackPressedCallback(true) {

            override fun handleOnBackPressed() {
                passVerificationStatusAndFinish()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(onBackPressedCallback)
    }
}