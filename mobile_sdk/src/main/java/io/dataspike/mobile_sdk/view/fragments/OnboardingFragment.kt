package io.dataspike.mobile_sdk.view.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayoutMediator
import io.dataspike.mobile_sdk.R
import io.dataspike.mobile_sdk.databinding.FragmentOnboardingBinding
import io.dataspike.mobile_sdk.dependencies_provider.DataspikeInjector
import io.dataspike.mobile_sdk.utils.disableOverscroll
import io.dataspike.mobile_sdk.utils.launchInMain
import io.dataspike.mobile_sdk.utils.setup
import io.dataspike.mobile_sdk.view.UIManager
import io.dataspike.mobile_sdk.view.adapters.OnboardingViewPager2Adapter
import io.dataspike.mobile_sdk.view.custom_views.TOP_BUTTON
import io.dataspike.mobile_sdk.view.ui_models.ButtonConfigModel
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
            tvStartVerificationTitle.setup(
                font = typography.header.font,
                textSize = typography.header.textSize,
                textColor = typography.header.textColor,
            )
            tlTimer.isVisible = UIManager.getUiConfig().options.showTimer
            blButtons.setup(
                topButtonConfig = ButtonConfigModel(
                    isVisible = true,
                    isEnabled = false,
                    isTransparent = false,
                    backgroundColors = Pair(palette.mainColor, palette.lightMainColor),
                    text = getString(R.string.start_verification),
                    textColors = Pair(palette.backgroundColor, palette.backgroundColor),
                    action = ::navigateToVerification,
                ),
                requirementsIsVisible = true,
                requirementsAction = { openRequirementsScreen(getRequirementsType()) },
            )
            tlTerms.setBothBoxesAreCheckedListener { bothBoxesAreChecked ->
                blButtons.setButtonEnabled(TOP_BUTTON, bothBoxesAreChecked)
            }
        }

        viewModel.setVerificationTimer()
    }

    override fun onResume() {
        super.onResume()
        setupViewPager()
    }

    private fun setupViewPager() {
        with(viewBinding ?: return) {
            vpOnboarding.adapter = OnboardingViewPager2Adapter()
            vpOnboarding.disableOverscroll()
            TabLayoutMediator(tlIndicators, vpOnboarding) { _, _ -> }.attach()
            tlIndicators.setup(
                selectedColor = palette.mainColor,
                deselectedColor = palette.lightMainColor,
            )
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