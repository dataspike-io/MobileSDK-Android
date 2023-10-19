package io.dataspike.mobile_sdk.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import io.dataspike.mobile_sdk.DataspikeVerificationStatus
import io.dataspike.mobile_sdk.R
import io.dataspike.mobile_sdk.databinding.FragmentVerificationExpiredBinding
import io.dataspike.mobile_sdk.domain.setVerificationResult
import io.dataspike.mobile_sdk.view.ui_models.TimerUiModel
import io.dataspike.mobile_sdk.view.view_models.EXPIRED

internal class VerificationExpiredFragment : BaseFragment() {

    private var viewBinding: FragmentVerificationExpiredBinding? = null

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
            tlTimer.setup(
                timerState = TimerUiModel(
                    timerString = requireContext().getString(R.string._00_00_00),
                    timerStatus = EXPIRED,
                )
            )
            ctalCompleteVerification.setup(completeAction = ::passVerificationStatusAndFinish)
        }

        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                passVerificationStatusAndFinish()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(onBackPressedCallback)
    }
}