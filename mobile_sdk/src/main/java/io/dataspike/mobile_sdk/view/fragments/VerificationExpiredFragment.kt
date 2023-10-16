package io.dataspike.mobile_sdk.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import io.dataspike.mobile_sdk.DataspikeVerificationStatus
import io.dataspike.mobile_sdk.R
import io.dataspike.mobile_sdk.databinding.FragmentVerificationExpiredBinding

internal class VerificationExpiredFragment : BaseFragment() {

    private var viewBinding: FragmentVerificationExpiredBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentVerificationExpiredBinding.inflate(inflater, container, false)
        return viewBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(viewBinding?: return) {
            with(clTimer) {
                tvTimerExpired.visibility = View.VISIBLE
                tvTimerExpired.text = requireContext().getString(
                    R.string.time_left,
                    "00:00:00"
                )
            }

            clCompleteVerification.mbComplete.setOnClickListener {
                setVerificationStatusAndFinish(
                    DataspikeVerificationStatus.VERIFICATION_EXPIRED
                )
            }
        }

        //TODO test
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                setVerificationStatusAndFinish(
                    DataspikeVerificationStatus.VERIFICATION_EXPIRED
                )
            }
        }

        activity?.onBackPressedDispatcher?.addCallback(onBackPressedCallback)
    }
}