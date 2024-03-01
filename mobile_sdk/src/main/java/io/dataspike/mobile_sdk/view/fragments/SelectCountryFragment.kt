package io.dataspike.mobile_sdk.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import io.dataspike.mobile_sdk.R
import io.dataspike.mobile_sdk.databinding.FragmentSelectCountryBinding
import io.dataspike.mobile_sdk.domain.models.EmptyState
import io.dataspike.mobile_sdk.utils.launchInMain
import io.dataspike.mobile_sdk.utils.setup
import io.dataspike.mobile_sdk.view.POI_FRONT
import io.dataspike.mobile_sdk.view.adapters.CountriesListAdapter
import io.dataspike.mobile_sdk.view.custom_views.TOP_BUTTON
import io.dataspike.mobile_sdk.view.ui_models.ButtonConfigModel
import io.dataspike.mobile_sdk.view.view_models.DataspikeViewModelFactory
import io.dataspike.mobile_sdk.view.view_models.SelectCountryViewModel

internal class SelectCountryFragment: BaseFragment() {

    private var viewBinding: FragmentSelectCountryBinding? = null
    private val viewModel: SelectCountryViewModel by viewModels { DataspikeViewModelFactory() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentSelectCountryBinding.inflate(inflater, container, false)

        return viewBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        collectLoading(viewModel, viewBinding?.lvLoader)
        collectGetCountriesFlow()
        collectSetCountryFlow()

        viewModel.getCountries()

        with(viewBinding ?: return) {
            hlTextHeader.setup(
                popBackStackAction = ::popBackStack,
                stringResId = R.string.select_country_title,
                colorInt = typography.header.textColor,
            )
            clSteps.setup(step = POI_FRONT)
            with(etSearch) {
                setup(
                    backgroundColor = palette.lighterMainColor,
                    font = typography.bodyTwo.font,
                    textSize = typography.bodyTwo.textSize,
                    textColor = typography.bodyTwo.textColor,
                    hintColor = typography.bodyTwo.textColor,
                )
                compoundDrawablesRelative[2]?.setTint(palette.mainColor)
                doOnTextChanged { text, _, _, _ -> viewModel.updateCountriesList(text) }
            }
            tvSearch.setup(
                font = typography.bodyOne.font,
                textSize = typography.bodyOne.textSize,
                textColor = typography.bodyOne.textColor,
            )
            bButtons.setup(
                topButtonConfig = ButtonConfigModel(
                    isVisible = true,
                    isEnabled = false,
                    isTransparent = false,
                    backgroundColors = Pair(palette.mainColor, palette.lightMainColor),
                    text = getString(R.string._continue),
                    textColors = Pair(palette.backgroundColor, palette.backgroundColor),
                    action = { viewModel.setCountry() }
                )
            )
        }
    }

    private fun collectGetCountriesFlow() {
        launchInMain {
            viewModel.getCountriesFlow.collect { countries ->
                with(viewBinding ?: return@collect) {
                    rvCountries.layoutManager = LinearLayoutManager(requireContext())
                    rvCountries.adapter = CountriesListAdapter(countries) { country ->
                        updateSelectedCountry(country)
                    }
                }
            }
        }
    }

    private fun collectSetCountryFlow() {
        launchInMain {
            viewModel.setCountryFlow.collect { emptyState ->
                when (emptyState) {
                    is EmptyState.EmptyStateSuccess -> {
                        popBackStack()
                    }

                    is EmptyState.EmptyStateError -> {
                        makeToast("${emptyState.details}: ${emptyState.error}")
                    }
                }
            }
        }
    }

    private fun updateSelectedCountry(countryCode: String?) {
        viewModel.setCountrySelected(countryCode)

        viewBinding?.bButtons?.setButtonEnabled(TOP_BUTTON, true)
    }
}