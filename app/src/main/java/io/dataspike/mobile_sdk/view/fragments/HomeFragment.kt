package io.dataspike.mobile_sdk.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import io.dataspike.mobile_sdk.DataspikeDependencies
import io.dataspike.mobile_sdk.DataspikeManager
import io.dataspike.mobile_sdk.DataspikeVerificationStatus
import io.dataspike.mobile_sdk.R
import io.dataspike.mobile_sdk.VerificationCompletedCallback
import io.dataspike.mobile_sdk.databinding.FragmentHomeBinding
import io.dataspike.mobile_sdk.dependencies_provider.SampleAppInjector
import io.dataspike.mobile_sdk.dependencies_provider.model.SampleAppDependencies
import io.dataspike.mobile_sdk.view.models.NewVerificationUiState
import io.dataspike.mobile_sdk.view.view_models.HomeViewModel
import io.dataspike.mobile_sdk.view.view_models.SampleAppViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

internal class HomeFragment : Fragment(), VerificationCompletedCallback {

    private var viewBinding: FragmentHomeBinding? = null
    private val viewModel: HomeViewModel by viewModels { SampleAppViewModelFactory() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentHomeBinding.inflate(inflater, container, false)

        return viewBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        SampleAppInjector.setComponent(SampleAppDependencies.dependencies)
        collectVerificationFlow()
        setUiState()
    }

    override fun onVerificationCompleted(
        dataspikeVerificationStatus: DataspikeVerificationStatus?
    ) {
        Toast.makeText(
            requireContext(),
            "verificationStatus: $dataspikeVerificationStatus",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun setUiState() {
        with(viewBinding ?: return) {
            cbIsDebug.setOnCheckedChangeListener { _, isChecked ->
                viewModel.onIsDebugChanged(isChecked)
            }
            tvShortId.doOnTextChanged { text, _, _, _ ->
                viewModel.onShortIdChanged(text?.toString().orEmpty())
            }
            bStartVerification.setOnClickListener {
                viewModel.createVerification()
            }

            with(tvApiToken) {
                doOnTextChanged { text, _, _, _ ->
                    bStartVerification.isEnabled =
                        text?.isNotEmpty() == true
                                || SampleAppDependencies.dependencies.dsApiToken.isNotEmpty()

                    viewModel.onApiTokenChanged(
                        text?.toString() ?: SampleAppDependencies.dependencies.dsApiToken
                    )
                }

                if (SampleAppDependencies.dependencies.dsApiToken.isEmpty()) {
                    tvApiToken.hint = getString(R.string.enter_api_token_required)
                    bStartVerification.isEnabled = false
                }
            }
        }
    }

    private fun launchInMain(block: suspend CoroutineScope.() -> Unit) {
        lifecycleScope.launch(Dispatchers.Main, block = block)
    }

    private fun collectVerificationFlow() {
        launchInMain {
            viewModel.verificationFlow.collect { newVerificationUiState ->
                when (newVerificationUiState) {
                    is NewVerificationUiState.NewVerificationUiSuccess -> {
                        with(viewBinding ?: return@collect) {
                            startDataspikeFlow(
                                isDebug = cbIsDebug.isChecked,
                                apiToken = tvApiToken.text.toString().ifEmpty { null }
                                    ?: SampleAppDependencies.dependencies.dsApiToken,
                                shortId = newVerificationUiState.shortId,
                            )
                        }
                    }

                    is NewVerificationUiState.NewVerificationUiError -> {
                        Toast.makeText(
                            requireContext(),
                            "${newVerificationUiState.details}: ${newVerificationUiState.error}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
    }

    private fun startDataspikeFlow(shortId: String, apiToken: String, isDebug: Boolean) {
        DataspikeManager.startDataspikeFlow(
            DataspikeDependencies(
                isDebug = isDebug,
                dsApiToken = apiToken,
                shortId = shortId,
            ),
            this@HomeFragment,
            requireContext()
        )
    }
}