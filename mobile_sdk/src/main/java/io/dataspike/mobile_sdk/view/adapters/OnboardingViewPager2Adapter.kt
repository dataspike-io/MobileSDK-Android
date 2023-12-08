package io.dataspike.mobile_sdk.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import io.dataspike.mobile_sdk.R
import io.dataspike.mobile_sdk.databinding.OnboardingPageLayoutBinding
import io.dataspike.mobile_sdk.dependencies_provider.DataspikeInjector
import io.dataspike.mobile_sdk.view.LIVENESS
import io.dataspike.mobile_sdk.view.POA
import io.dataspike.mobile_sdk.view.POI_FRONT
import io.dataspike.mobile_sdk.view.ui_models.OnboardingUiModel

internal class OnboardingViewPager2Adapter:
    RecyclerView.Adapter<OnboardingViewPager2Adapter.ViewHolder>() {

    private val items: MutableMap<Int, OnboardingUiModel> = mutableMapOf()

    init {
        var position = 0

        with(DataspikeInjector.component.verificationManager.checks) {
            if (poiIsRequired) {
                items[position++] = OnboardingUiModel(
                    drawableResId = R.drawable.onboarding_poi,
                    stringResId = R.string.onboarding_step_1,
                    pageType = POI_FRONT,
                )
            }

            if (livenessIsRequired) {
                items[position++] = OnboardingUiModel(
                    drawableResId = R.drawable.onboarding_liveness,
                    stringResId = R.string.onboarding_step_2,
                    pageType = LIVENESS
                )
            }

            if (poaIsRequired) {
                items[position] = OnboardingUiModel(
                    drawableResId = R.drawable.onboarding_poa,
                    stringResId = R.string.onboarding_step_3,
                    pageType = POA
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
            val drawableRes = items[position]?.drawableResId ?: -1
            val textRes = items[position]?.stringResId ?: -1

            ivOnboardingImage.setImageDrawable(
                ContextCompat.getDrawable(holder.itemView.context, drawableRes)
            )

            tvOnboardingText.text = itemView.resources.getString(textRes)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun getCurrentPage(position: Int): String? {
        return items[position]?.pageType
    }

    inner class ViewHolder(binding: OnboardingPageLayoutBinding):
        RecyclerView.ViewHolder(binding.root) {
        val ivOnboardingImage = binding.ivOnboardingImage
        val tvOnboardingText = binding.tvOnboardingText
    }
}