package io.dataspike.mobile_sdk.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import io.dataspike.mobile_sdk.DataspikeManager
import io.dataspike.mobile_sdk.databinding.FragmentHomeBinding
import io.dataspike.mobile_sdk.dependencies_provider.SampleAppInjector
import io.dataspike.mobile_sdk.dependencies_provider.model.DataspikeDependencies
import io.dataspike.mobile_sdk.dependencies_provider.model.SampleAppDependencies
import io.dataspike.mobile_sdk.domain.models.NewVerificationState
import io.dataspike.mobile_sdk.view.view_models.HomeViewModel
import io.dataspike.mobile_sdk.view.view_models.SampleAppViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

internal const val API_TOKEN = "place_your_api_token_here"

internal class HomeFragment : Fragment() {

    private var viewBinding: FragmentHomeBinding? = null

    private val viewModel: HomeViewModel by viewModels{ SampleAppViewModelFactory() }

    private val sampleAppDependencies = SampleAppDependencies(
        isDebug = true,
        dsApiToken = API_TOKEN,
    )

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

       SampleAppInjector.setComponent(sampleAppDependencies)

        collectVerificationFlow()

        viewBinding?.tvStartVerificationButton?.setOnClickListener {
            viewModel.createVerification(emptyMap())
        }
    }

    private fun launchInMain(block: suspend CoroutineScope.() -> Unit) {
        lifecycleScope.launch(Dispatchers.Main, block = block)
    }

    private fun collectVerificationFlow() {
        launchInMain {
            viewModel.verificationFlow.collect { result ->
                when (result) {
                    is NewVerificationState.NewVerificationSuccess -> {
                        DataspikeManager.startDataspikeFlow(
                            DataspikeDependencies(
                                isDebug = true,
                                dsApiToken = API_TOKEN,
                                shortId = result.verificationUrlId,
                            ),
                            requireContext()
                        )
                    }

                    is NewVerificationState.NewVerificationError -> {
                        Toast.makeText(
                            requireContext(),
                            "${result.error}: ${result.validationError}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
    }
}