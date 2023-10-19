package io.dataspike.mobile_sdk.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import io.dataspike.mobile_sdk.databinding.FragmentVerificationCompleteBinding
import io.dataspike.mobile_sdk.domain.setVerificationResult
import io.dataspike.mobile_sdk.utils.launchInMain
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
        collectUploadImageFlow()
        viewModel.proceedWithVerification()
        viewBinding?.ctalCompleteTryAgain?.setup(
            completeAction = ::passVerificationStatusAndFinish,
            tryAgainAction = ::retryVerification,
        )

        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                passVerificationStatusAndFinish()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(onBackPressedCallback)
    }

    private fun collectUploadImageFlow() {
        launchInMain {
            viewModel.proceedWithVerificationFlow.collect { result ->
                when (result) {
                    is ProceedWithVerificationUiState.ProceedWithVerificationUiSuccess -> {
                        setVerificationResult(result.verificationStatus)
                    }

                    is ProceedWithVerificationUiState.ProceedWithVerificationUiError -> {
                        setErrorUiState()
                    }
                }
            }
        }
    }

    private fun setErrorUiState() {
        with(viewBinding ?: return) {
            ctalCompleteTryAgain.setup(
                completeAction = ::passVerificationStatusAndFinish,
                tryAgainAction = ::retryVerification,
                verificationFailed = true,
            )
            vrlVerificationResult.setupError()
        }
    }
}