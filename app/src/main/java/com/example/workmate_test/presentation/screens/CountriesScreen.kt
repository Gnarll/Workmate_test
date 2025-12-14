package com.example.workmate_test.presentation.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.workmate_test.domain.utils.Result
import com.example.workmate_test.presentation.ui.components.CountriesList
import com.example.workmate_test.presentation.viewmodels.CountriesViewModel

@Composable
fun CountriesScreen(
    onNavigateToCountryDetails: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CountriesViewModel = hiltViewModel(),
) {
    val countriesState = viewModel.countriesStream.collectAsState(Result.Loading)

    CountriesList(
        countriesProvider = { countriesState.value },
        onClickCountry = { country -> onNavigateToCountryDetails(country.id) },
        onRefresh = viewModel::onRefresh,
        modifier = modifier.fillMaxSize()
    )

}