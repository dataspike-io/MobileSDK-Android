package io.dataspike.mobile_sdk.view.view_models

import io.dataspike.mobile_sdk.data.models.requests.CountryRequestBody
import io.dataspike.mobile_sdk.data.use_cases.GetCountriesUseCase
import io.dataspike.mobile_sdk.data.use_cases.SetCountryUseCase
import io.dataspike.mobile_sdk.domain.models.CountriesState
import io.dataspike.mobile_sdk.domain.models.CountryDomainModel
import io.dataspike.mobile_sdk.domain.models.EmptyState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

internal class SelectCountryViewModel(
    private val getCountriesUseCase: GetCountriesUseCase,
    private val setCountryUseCase: SetCountryUseCase,
): BaseViewModel() {

    private val countries = mutableListOf<CountryDomainModel>()
    private val _getCountriesFlow = MutableSharedFlow<List<CountryDomainModel>>(replay = 1)
    val getCountriesFlow: SharedFlow<List<CountryDomainModel>> = _getCountriesFlow
    private val _setCountryFlow = MutableSharedFlow<EmptyState>(replay = 1)
    val setCountryFlow: SharedFlow<EmptyState> = _setCountryFlow

    fun getCountries() {
        showLoading(true)

        launchInVMScope {
            countries.addAll(
                (getCountriesUseCase.invoke() as? CountriesState.CountriesSuccess)
                    ?.countries ?: emptyList()
            )

            _getCountriesFlow.emit(countries)
        }

        showLoading(false)
    }

    fun setCountry() {
        showLoading(true)

        val selectedCountry =
            countries.firstOrNull { country -> country.isSelected }?.alphaTwo ?: return

        launchInVMScope {
            _setCountryFlow.emit(
                setCountryUseCase.invoke(
                    CountryRequestBody(country = selectedCountry)
                )
            )

            showLoading(false)
        }
    }

    fun updateCountriesList(text: CharSequence?) {
        launchInVMScope {
            _getCountriesFlow.emit(
                countries.filter { it.name.startsWith(text ?: "", true) }
            )
        }
    }

    fun setCountrySelected(countryCode: String?) {
        countries.forEach { country ->
            country.isSelected = country.alphaTwo == countryCode
        }
    }
}