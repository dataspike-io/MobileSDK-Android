package io.dataspike.mobile_sdk.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import io.dataspike.mobile_sdk.R
import io.dataspike.mobile_sdk.databinding.OnboardingPageLayoutBinding


internal class ViewPager2Adapter: RecyclerView.Adapter<ViewPager2Adapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = OnboardingPageLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            val drawableRes = when (position) {
                0 -> { R.drawable.onboarding_poi }

                1 -> { R.drawable.onboarding_liveness }

                2 -> { R.drawable.onboarding_poa }

                else -> { return }
            }

            val textRes = when (position) {
                0 -> { R.string.onboarding_step_1 }

                1 -> { R.string.onboarding_step_2 }

                2 -> { R.string.onboarding_step_3 }

                else -> { return }
            }

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

    override fun getItemCount(): Int = 3

    inner class ViewHolder(binding: OnboardingPageLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val ivOnboardingImage = binding.ivOnboardingImage
        val tvOnboardingText = binding.tvOnboardingText
    }
}