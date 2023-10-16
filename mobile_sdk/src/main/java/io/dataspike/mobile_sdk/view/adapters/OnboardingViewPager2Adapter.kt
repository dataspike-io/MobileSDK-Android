package io.dataspike.mobile_sdk.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import io.dataspike.mobile_sdk.R
import io.dataspike.mobile_sdk.databinding.OnboardingPageLayoutBinding
import io.dataspike.mobile_sdk.domain.VerificationManager
import io.dataspike.mobile_sdk.view.LIVENESS
import io.dataspike.mobile_sdk.view.POA
import io.dataspike.mobile_sdk.view.POI_FRONT

internal class OnboardingViewPager2Adapter:
    RecyclerView.Adapter<OnboardingViewPager2Adapter.ViewHolder>() {

    private val items: MutableMap<Int, Triple<Int, Int, String>> = mutableMapOf()

    init {
        var position = 0

        with(VerificationManager.checks) {
            if (poiIsRequired) {
                items[position++] = Triple(
                    R.drawable.onboarding_poi,
                    R.string.onboarding_step_1,
                    POI_FRONT
                )
            }
            if (livenessIsRequired) {
                items[position++] = Triple(
                    R.drawable.onboarding_liveness,
                    R.string.onboarding_step_2,
                    LIVENESS
                )
            }
            if (poaIsRequired) {
                items[position] = Triple(
                    R.drawable.onboarding_poa,
                    R.string.onboarding_step_3,
                    POA
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = OnboardingPageLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            val drawableRes = items[position]?.first ?: -1
            val textRes = items[position]?.second ?: -1

            ivOnboardingImage.setImageDrawable(
                ResourcesCompat.getDrawable(
                    holder.itemView.resources,
                    drawableRes,
                    null
                )
            )

            tvOnboardingText.text = itemView.resources.getString(textRes)
        }
    }

    override fun getItemCount(): Int = items.size

    fun getCurrentPage(position: Int) = items[position]?.third

    inner class ViewHolder(binding: OnboardingPageLayoutBinding):
        RecyclerView.ViewHolder(binding.root) {
        val ivOnboardingImage = binding.ivOnboardingImage
        val tvOnboardingText = binding.tvOnboardingText
    }
}