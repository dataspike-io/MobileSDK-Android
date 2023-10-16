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
import io.dataspike.mobile_sdk.utils.Utils.launchInMain
import io.dataspike.mobile_sdk.view.POI_FRONT
import io.dataspike.mobile_sdk.view.adapters.CountriesListAdapter
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

        collectGetCountriesFlow()
        collectSetCountryFlow()

        viewModel.getCountries()

        with(viewBinding ?: return) {
            with(clUploadResult) {
                tvUploadSuccessful.visibility = View.GONE
                tvUploadWithErrors.visibility = View.GONE
                tvCountryNotIdentified.visibility = View.VISIBLE
            }

            with(clBlackTextHeaderLayout) {
                tvTopInstructions.text = requireContext().getString(R.string.select_country_title)
                ivBackButton.setOnClickListener {
                    parentFragmentManager.popBackStack()
                }
            }

            etSearch.doOnTextChanged { text, _, _, _ ->
                viewModel.updateCountriesList(text)
            }

            clSteps.setStepsState(POI_FRONT)

            mbContinue.isEnabled = false

            mbContinue.setOnClickListener {
                viewModel.setCountry()
            }
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
            viewModel.setCountryFlow.collect {
                parentFragmentManager.popBackStack()
            }
        }
    }

    private fun updateSelectedCountry(countryCode: String?) {
        countryCode ?: return

        viewModel.setCountrySelected(countryCode)

        viewBinding?.mbContinue?.isEnabled = true
    }
}