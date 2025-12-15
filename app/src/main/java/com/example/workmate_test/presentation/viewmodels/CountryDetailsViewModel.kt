package com.example.workmate_test.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workmate_test.domain.models.Country
import com.example.workmate_test.domain.repositiories.CountryRepository
import com.example.workmate_test.domain.utils.Result
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = CountryDetailsViewModel.CountryDetailsFactory::class)
class CountryDetailsViewModel @AssistedInject constructor(
    private val countryRepository: CountryRepository,
    @Assisted private val countryId: Int
) :
    ViewModel() {
    private val _country: MutableStateFlow<Result<Country>> = MutableStateFlow(Result.Loading)
    val country = _country

    init {
        loadCountry()
    }

    private fun loadCountry() {
        viewModelScope.launch {
            _country.value = countryRepository.getCountry(countryId)
        }
    }

    @AssistedFactory
    interface CountryDetailsFactory {
        fun create(countryId: Int): CountryDetailsViewModel
    }
}