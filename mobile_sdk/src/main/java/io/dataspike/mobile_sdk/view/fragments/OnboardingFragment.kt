package io.dataspike.mobile_sdk.view.fragments

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayoutMediator
import io.dataspike.mobile_sdk.R
import io.dataspike.mobile_sdk.databinding.FragmentOnboardingBinding
import io.dataspike.mobile_sdk.domain.VerificationManager
import io.dataspike.mobile_sdk.view.adapters.OnboardingViewPager2Adapter

private const val ACTIVE = "active"
private const val COMPLETED = "completed"

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

            clButtons.mbContinue.setOnClickListener { goToVerification() }

            clButtons.tvRequirements.setOnClickListener {
                val requirementsType =
                    (vpOnboarding.adapter as? OnboardingViewPager2Adapter)?.getCurrentPage(
                        vpOnboarding.currentItem
                    )

                openRequirementsScreen(requirementsType)
            }

            setTimer(this)
        }
    }

    private fun setTimer(viewBinding: FragmentOnboardingBinding) {
        with(viewBinding.clTimer) {
            val verificationStatus = VerificationManager.status
            val millisecondsUntilVerificationExpired =
                VerificationManager.millisecondsUntilVerificationExpired

            when {
                verificationStatus == ACTIVE && millisecondsUntilVerificationExpired > 0 -> {
                    tvTimer.visibility = View.VISIBLE
                    tvTimerExpired.visibility = View.GONE

                    val timer = object : CountDownTimer(
                        millisecondsUntilVerificationExpired,
                        1000
                    ) {
                        override fun onTick(millisUntilFinished: Long) {
                            val seconds = millisUntilFinished / 1000
                            val hours = seconds / 3600
                            val minutes = (seconds % 3600) / 60
                            val remainingSeconds = seconds % 60
                            val formattedTime =
                                String.format("%02d:%02d:%02d", hours, minutes, remainingSeconds)

                            //TODO fix not attached to a context
                            tvTimer.text = context?.applicationContext?.getString(
                                R.string.time_left,
                                formattedTime
                            )
                        }

                        override fun onFinish() {
                            tvTimer.visibility = View.GONE
                            tvTimerExpired.visibility = View.VISIBLE
                            tvTimerExpired.text = requireContext().getString(
                                R.string.time_left,
                                "00:00:00"
                            )

                            if (this@OnboardingFragment.isResumed) {
                                navigateToFragment(VerificationExpiredFragment())
                            }
                        }
                    }

                    timer.start()
                }

                verificationStatus == COMPLETED -> {
                    navigateToFragment(VerificationCompleteFragment())
                }

                else -> {
                    tvTimer.visibility = View.GONE
                    tvTimerExpired.visibility = View.VISIBLE
                    tvTimerExpired.text = requireContext().getString(
                        R.string.time_left,
                        "00:00:00"
                    )

                    navigateToFragment(VerificationExpiredFragment())
                }
            }
        }
    }

    private fun goToVerification() {
        val fragmentToNavigateTo = when {
            VerificationManager.checks.poiIsRequired -> {
                POIIntroFragment()
            }

            VerificationManager.checks.livenessIsRequired -> {
                LivenessVerificationFragment()
            }

            VerificationManager.checks.poaIsRequired -> {
                POAChooserFragment()
            }

            else -> {
                Log.d("Dataspike", "Verification has no required checks")
                return
            }
        }

        navigateToFragment(fragmentToNavigateTo, false)
    }
}