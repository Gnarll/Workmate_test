package com.example.workmate_test.presentation.viewmodels

import androidx.lifecycle.ViewModel
import com.example.workmate_test.domain.repositiories.CountryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CountriesViewModel @Inject constructor(private val countryRepository: CountryRepository) :
    ViewModel() {
    val countriesStream = countryRepository.getCountriesSteam()

    suspend fun onRefresh() {
        countryRepository.refreshCountries()
    }
}