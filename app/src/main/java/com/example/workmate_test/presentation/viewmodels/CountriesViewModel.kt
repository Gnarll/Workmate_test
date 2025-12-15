package com.example.workmate_test.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workmate_test.domain.repositiories.CountryRepository
import com.example.workmate_test.domain.utils.Result
import com.example.workmate_test.presentation.ui.states.CountriesUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CountriesViewModel @Inject constructor(private val countryRepository: CountryRepository) :
    ViewModel() {

    private val _countriesUiState: MutableStateFlow<CountriesUiState> =
        MutableStateFlow(CountriesUiState(countriesResult = Result.Loading, isRefreshing = false))
    val countriesUiState: StateFlow<CountriesUiState> = _countriesUiState

    init {
        viewModelScope.launch {
            countryRepository.getCountriesSteam().collect { countriesResult ->
                _countriesUiState.update {
                    it.copy(
                        countriesResult = countriesResult,
                        isRefreshing = countriesResult is Result.Loading
                    )
                }

            }
        }
    }

    suspend fun onRefresh() {
        _countriesUiState.update {
            it.copy(isRefreshing = true)
        }
        countryRepository.refreshCountries()
    }
}