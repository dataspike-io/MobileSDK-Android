package io.dataspike.mobile_sdk.view.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayoutMediator
import io.dataspike.mobile_sdk.R
import io.dataspike.mobile_sdk.databinding.FragmentOnboardingBinding
import io.dataspike.mobile_sdk.dependencies_provider.DataspikeInjector
import io.dataspike.mobile_sdk.utils.disableOverscroll
import io.dataspike.mobile_sdk.utils.launchInMain
import io.dataspike.mobile_sdk.view.adapters.OnboardingViewPager2Adapter
import io.dataspike.mobile_sdk.view.view_models.DataspikeViewModelFactory
import io.dataspike.mobile_sdk.view.view_models.OnboardingViewModel

internal class OnboardingFragment : BaseFragment() {

    private var viewBinding: FragmentOnboardingBinding? = null
    private val viewModel: OnboardingViewModel by viewModels {
        DataspikeViewModelFactory()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentOnboardingBinding.inflate(inflater, container, false)

        return viewBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        collectTimerFlow()

        with(viewBinding ?: return) {
            vpOnboarding.adapter = OnboardingViewPager2Adapter()
            vpOnboarding.disableOverscroll()
            TabLayoutMediator(tlIndicators, vpOnboarding) { _, _ -> }.attach()

            roblButtons.setup(
                stringResId = R.string._continue,
                continueButtonAction = ::navigateToVerification,
            ) {
                openRequirementsScreen(getRequirementsType())
            }

            viewModel.setVerificationTimer()
        }
    }

    private fun collectTimerFlow() {
        launchInMain {
            viewModel.timerFlow.collect { timerState ->
                viewBinding?.tlTimer?.setup(
                    timerState = timerState,
                    completedAction = ::navigateToVerificationCompleteFragment,
                    expiredAction = ::navigateToVerificationExpiredFragment,
                )
            }
        }
    }

    private fun getRequirementsType(): String? {
        with(viewBinding?.vpOnboarding ?: return null) {
            return (adapter as? OnboardingViewPager2Adapter)?.getCurrentPage(currentItem)
        }
    }

    private fun navigateToVerificationCompleteFragment() {
        navigateToFragment(VerificationCompleteFragment())
    }

    private fun navigateToVerificationExpiredFragment() {
        navigateToFragment(VerificationExpiredFragment())
    }

    private fun navigateToVerification() {
        val verificationManager = DataspikeInjector.component.verificationManager
        val fragmentToNavigateTo = when {
            verificationManager.checks.poiIsRequired -> {
                PoiIntroFragment()
            }

            verificationManager.checks.livenessIsRequired -> {
                LivenessVerificationFragment()
            }

            verificationManager.checks.poaIsRequired -> {
                PoaChooserFragment()
            }

            else -> {
                Log.d("Dataspike", "Verification has no required checks")

                return
            }
        }

        navigateToFragment(fragmentToNavigateTo, false)
    }
}