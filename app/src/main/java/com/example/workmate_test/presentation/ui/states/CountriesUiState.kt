package com.example.workmate_test.presentation.ui.states

import androidx.compose.runtime.Immutable
import com.example.workmate_test.domain.models.Country
import com.example.workmate_test.domain.utils.Result

@Immutable
data class CountriesUiState(
    val countriesResult: Result<List<Country>>,
    val isRefreshing: Boolean
)