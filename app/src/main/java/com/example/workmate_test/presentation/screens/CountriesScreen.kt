package com.example.workmate_test.presentation.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.workmate_test.presentation.ui.components.CountriesList
import com.example.workmate_test.presentation.viewmodels.CountriesViewModel
import kotlinx.coroutines.launch

@Composable
fun CountriesScreen(
    onNavigateToCountryDetails: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CountriesViewModel = hiltViewModel(),
) {
    val coroutineScope = rememberCoroutineScope()
    val uiState = viewModel.countriesUiState.collectAsState()

    CountriesList(
        countriesProvider = { uiState.value.countriesResult },
        isRefreshingProvider = { uiState.value.isRefreshing },
        onClickCountry = { country -> onNavigateToCountryDetails(country.id) },
        onRefresh = {
            coroutineScope.launch {
                viewModel.onRefresh()
            }
        },
        modifier = modifier.fillMaxSize()
    )

}