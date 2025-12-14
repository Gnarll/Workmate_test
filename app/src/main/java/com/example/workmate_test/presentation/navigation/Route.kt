package com.example.workmate_test.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation3.runtime.NavKey
import com.example.workmate_test.R
import kotlinx.serialization.Serializable

@Serializable
sealed interface Route : NavKey {

    @Composable
    fun getTitle(): String? = null

    @Serializable
    data object CountriesRoute : Route {
        @Composable
        override fun getTitle(): String? = stringResource(R.string.countries)
    }

    @Serializable
    data class CountryDetailsRoute(val countryId: Int) : Route
}