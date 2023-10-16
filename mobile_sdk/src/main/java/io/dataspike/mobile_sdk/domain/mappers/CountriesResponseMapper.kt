package io.dataspike.mobile_sdk.domain.mappers

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import io.dataspike.mobile_sdk.data.models.responses.CountryResponse
import io.dataspike.mobile_sdk.data.models.responses.UploadImageErrorResponse
import io.dataspike.mobile_sdk.domain.models.CountriesState
import io.dataspike.mobile_sdk.domain.models.CountryDomainModel
import retrofit2.HttpException
import java.util.Locale

internal object CountriesResponseMapper {

    fun map(result: Result<Array<CountryResponse>>): CountriesState {
        result
            .onSuccess { countryResponseArray ->
                return CountriesState.CountriesSuccess(
                    countries = countryResponseArray.map { countryResponse ->
                        CountryDomainModel(
                            alphaTwo = countryResponse.alphaTwo?.lowercase(Locale.ROOT) ?: "",
                            alphaThree = countryResponse.alphaThree ?: "",
                            name = countryResponse.name ?: "",
                            continent = countryResponse.continent ?: "",
                        )
                    }
                )
            }
            .onFailure { throwable ->
                val message = when (throwable) {
                    is HttpException -> {
                        throwable.toCountriesStateErrorMessage()
                    }

                    else -> "Unknown error occurred"
                }

                return CountriesState.CountriesError(
                    message = message
                )
            }

        throw IllegalStateException("Unknown error occurred")
    }

    private fun HttpException.toCountriesStateErrorMessage(): String {
        val uploadImageErrorResponse = try {
            Gson().fromJson(
                response()?.errorBody()?.string(),
                UploadImageErrorResponse::class.java
            )
        } catch (e: JsonSyntaxException) {
            null
        }

        return uploadImageErrorResponse?.errors?.get(0)?.message?.replaceFirstChar { char ->
            if (char.isLowerCase()) {
                char.titlecase()
            } else {
                char.toString()
            }
        } ?: "Unknown error occurred"
    }
}