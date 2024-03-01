package io.dataspike.mobile_sdk.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import io.dataspike.mobile_sdk.R
import io.dataspike.mobile_sdk.databinding.OnboardingPageLayoutBinding
import io.dataspike.mobile_sdk.dependencies_provider.DataspikeInjector
import io.dataspike.mobile_sdk.utils.setup
import io.dataspike.mobile_sdk.view.LIVENESS
import io.dataspike.mobile_sdk.view.POA
import io.dataspike.mobile_sdk.view.POI_FRONT
import io.dataspike.mobile_sdk.view.UIManager
import io.dataspike.mobile_sdk.view.ui_models.OnboardingUiModel

internal class OnboardingViewPager2Adapter:
    RecyclerView.Adapter<OnboardingViewPager2Adapter.ViewHolder>() {

    private val items: MutableMap<Int, OnboardingUiModel> = mutableMapOf()
    private val imageLinks = UIManager.getUiConfig().links.onboarding
    private val bodyTwoTypography = UIManager.getUiConfig().theme.typography.bodyTwo

    init {
        var position = 0

        with(DataspikeInjector.component.verificationManager.checks) {
            if (poiIsRequired) {
                items[position++] = OnboardingUiModel(
                    imageLink = imageLinks.poi,
                    stringResId = R.string.onboarding_step_1,
                    pageType = POI_FRONT,
                )
            }

            if (livenessIsRequired) {
                items[position++] = OnboardingUiModel(
                    imageLink = imageLinks.liveness,
                    stringResId = R.string.onboarding_step_2,
                    pageType = LIVENESS
                )
            }

            if (poaIsRequired) {
                items[position] = OnboardingUiModel(
                    imageLink = imageLinks.poa,
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
            val imageLink = items[position]?.imageLink ?: -1
            val textRes = items[position]?.stringResId ?: -1

            Glide
                .with(ivOnboardingImage)
                .load(imageLink)
                .into(ivOnboardingImage)
            with(tvOnboardingText) {
                setup(
                    font = bodyTwoTypography.font,
                    textSize = bodyTwoTypography.textSize,
                    textColor = bodyTwoTypography.textColor,
                )
                text = itemView.resources.getString(textRes, position + 1)
            }
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