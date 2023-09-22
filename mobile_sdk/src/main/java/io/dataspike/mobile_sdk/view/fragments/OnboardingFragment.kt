package io.dataspike.mobile_sdk.view.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayoutMediator
import io.dataspike.mobile_sdk.R
import io.dataspike.mobile_sdk.databinding.FragmentOnboardingBinding
import io.dataspike.mobile_sdk.domain.VerificationChecksManager
import io.dataspike.mobile_sdk.view.IMAGE_TYPE
import io.dataspike.mobile_sdk.view.OnboardingViewPager2Adapter
import io.dataspike.mobile_sdk.view.POI_FRONT

internal class OnboardingFragment : BaseFragment() {

    private var viewBinding: FragmentOnboardingBinding? = null

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

        with(viewBinding ?: return) {
            //TODO make onboarding show only required checks
            vpOnboarding.adapter = OnboardingViewPager2Adapter()
            (vpOnboarding.getChildAt(0) as? RecyclerView)?.overScrollMode =
                View.OVER_SCROLL_NEVER
            TabLayoutMediator(tlIndicators, vpOnboarding) { _, _ -> }.attach()

            mbStartVerification.setOnClickListener { goToVerification() }

            tvRequirements.setOnClickListener {
                val requirementsType =
                    (vpOnboarding.adapter as? OnboardingViewPager2Adapter)?.getCurrentPage(
                        vpOnboarding.currentItem
                    )

                openRequirementsScreen(requirementsType)
            }
        }
    }

    private fun goToVerification() {
        val fragmentToGoTo = when {
            VerificationChecksManager.checks.poiIsRequired -> {
                POIVerificationFragment().apply {
                    arguments = bundleOf(IMAGE_TYPE to POI_FRONT)
                }
            }

            VerificationChecksManager.checks.livenessIsRequired -> {
                LivenessVerificationFragment()
            }

            VerificationChecksManager.checks.poaIsRequired -> {
                POAVerificationFragment()
            }

            else -> {
                Log.d("Dataspike", "Verification has no required checks")
                return
            }
        }

        activity
            ?.supportFragmentManager
            ?.beginTransaction()
            ?.add(
                R.id.container,
                fragmentToGoTo
            )
            ?.addToBackStack(null)
            ?.commit()
    }
}