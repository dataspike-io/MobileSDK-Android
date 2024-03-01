package io.dataspike.mobile_sdk.view.adapters

import android.content.res.ColorStateList
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import io.dataspike.mobile_sdk.databinding.CountryItemBinding
import io.dataspike.mobile_sdk.domain.models.CountryDomainModel
import io.dataspike.mobile_sdk.utils.setup
import io.dataspike.mobile_sdk.view.UIManager

internal class CountriesListAdapter(
    private val countries: List<CountryDomainModel>? = emptyList(),
    private val onClickListener: (String?) -> Unit,
): RecyclerView.Adapter<CountriesListAdapter.ViewHolder>() {

    private val palette = UIManager.getUiConfig().theme.palette
    private val typography = UIManager.getUiConfig().theme.typography

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CountryItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        with(viewHolder) {
            val country = countries?.get(position)

            itemView.isSelected = country?.isSelected ?: false
            with(tvCountryName) {
                setup(
                    font = typography.bodyTwo.font,
                    textSize = typography.bodyTwo.textSize,
                    textColor = typography.bodyTwo.textColor,
                )
                text = country?.name ?: ""
            }

            Glide
                .with(ivFlag)
                .load("https://flagcdn.com/80x60/${country?.alphaTwo}.png")
                .into(ivFlag)
            with(countryItem) {
                val backgroundDrawable = GradientDrawable().apply {
                    shape = GradientDrawable.RECTANGLE
                    color = ColorStateList(
                        arrayOf(intArrayOf(android.R.attr.state_selected), intArrayOf()),
                        intArrayOf(palette.lighterMainColor, palette.backgroundColor)
                    )
                }

                background = backgroundDrawable
                setOnClickListener {
                    notifyItemChanged(
                        countries?.indexOfFirst { country -> country.isSelected } ?: 0
                    )
                    countries?.forEachIndexed { index, country ->
                        country.isSelected = index == adapterPosition
                    }
                    notifyItemChanged(adapterPosition)
                    onClickListener.invoke(country?.alphaTwo)
                }
            }
        }
    }
    override fun getItemCount(): Int {
        return countries?.size ?: 0
    }

    inner class ViewHolder(binding: CountryItemBinding): RecyclerView.ViewHolder(binding.root) {

        val ivFlag = binding.ivFlag
        val tvCountryName = binding.tvCountryName
        val countryItem = binding.llCountryItem
    }
}