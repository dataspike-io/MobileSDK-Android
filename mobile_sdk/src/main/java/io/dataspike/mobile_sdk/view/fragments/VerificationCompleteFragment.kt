package io.dataspike.mobile_sdk.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import io.dataspike.mobile_sdk.DataspikeVerificationStatus
import io.dataspike.mobile_sdk.R
import io.dataspike.mobile_sdk.databinding.FragmentVerificationCompleteBinding
import io.dataspike.mobile_sdk.domain.setVerificationResult
import io.dataspike.mobile_sdk.utils.launchInMain
import io.dataspike.mobile_sdk.view.ui_models.ButtonConfigModel
import io.dataspike.mobile_sdk.view.ui_models.ProceedWithVerificationUiState
import io.dataspike.mobile_sdk.view.view_models.DataspikeViewModelFactory
import io.dataspike.mobile_sdk.view.view_models.VerificationCompleteViewModel

internal class VerificationCompleteFragment : BaseFragment() {

    private var viewBinding: FragmentVerificationCompleteBinding? = null
    private val viewModel: VerificationCompleteViewModel by viewModels {
        DataspikeViewModelFactory()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentVerificationCompleteBinding.inflate(
            inflater,
            container,
            false
        )
        return viewBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        collectProceedWithVerificationFlow()
        viewModel.proceedWithVerification()
        viewBinding?.bButtons?.setup(
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

        val onBackPressedCallback = object : OnBackPressedCallback(true) {

            override fun handleOnBackPressed() {
                passVerificationStatusAndFinish()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(onBackPressedCallback)
    }

    private fun collectProceedWithVerificationFlow() {
        launchInMain {
            viewModel.proceedWithVerificationFlow.collect { proceedWithVerificationUiState ->
                when (proceedWithVerificationUiState) {
                    is ProceedWithVerificationUiState.ProceedWithVerificationUiSuccess -> {
                        setVerificationResult(proceedWithVerificationUiState.verificationStatus)

                        if (
                            proceedWithVerificationUiState.verificationStatus ==
                            DataspikeVerificationStatus.VERIFICATION_EXPIRED
                        ) {
                            navigateToFragment(VerificationExpiredFragment())
                        }
                    }

                    is ProceedWithVerificationUiState.ProceedWithVerificationUiError -> {
                        if (proceedWithVerificationUiState.shouldNavigateToSelectCountryFragment) {
                            navigateToFragment(SelectCountryFragment())
                        } else {
                            makeToast(getString(R.string.unknown_error_occurred))
                            popBackStack()
                        }
                    }
                }
            }
        }
    }
}