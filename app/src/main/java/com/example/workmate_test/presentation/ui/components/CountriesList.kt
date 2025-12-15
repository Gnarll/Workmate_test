package com.example.workmate_test.presentation.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.example.workmate_test.R
import com.example.workmate_test.domain.models.Country
import com.example.workmate_test.domain.utils.Result

@Composable
fun CountriesList(
    countriesProvider: () -> Result<List<Country>>,
    isRefreshingProvider: () -> Boolean,
    onRefresh: () -> Unit,
    onClickCountry: (Country) -> Unit,
    modifier: Modifier = Modifier
) {


    val isRefreshing = isRefreshingProvider()
    val countries = countriesProvider()


    PullToRefreshBox(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize(),
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
    ) {
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.list_padding_between_items)),
            modifier = Modifier.fillMaxSize()
        ) {
            when (countries) {
                is Result.Loading -> {
                    item {
                        CircularProgressIndicator()
                    }
                }


                is Result.Success -> {
                    items(
                        count = countries.data.size,
                        key = { index -> countries.data[index].id }
                    ) { index ->
                        val country = countries.data[index]

                        CountryItem(country, onClick = onClickCountry)
                    }

                }

                is Result.Error.NetworkError -> {
                    item {
                        Text(
                            text = stringResource(R.string.error_network),
                            color = MaterialTheme.colorScheme.error,
                        )
                    }

                }

                is Result.Error.SerializationError -> {
                    item {
                        Text(
                            text = stringResource(R.string.error_serialization),
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }

                is Result.Error.ServerError -> {
                    item {
                        Text(
                            text = stringResource(R.string.error_server),
                            color = MaterialTheme.colorScheme.error
                        )
                    }

                }

                is Result.Error.UnknownError -> {
                    item {
                        Text(
                            text = stringResource(R.string.error_unknown),
                            color = MaterialTheme.colorScheme.error
                        )
                    }


                }

            }


        }
    }
}
