package com.example.workmate_test.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.request.error
import coil3.request.fallback
import coil3.request.placeholder
import com.example.workmate_test.R
import com.example.workmate_test.domain.utils.Result
import com.example.workmate_test.presentation.ui.components.InfoTextInput
import com.example.workmate_test.presentation.viewmodels.CountryDetailsViewModel

@Composable
fun CountryDetailsScreen(
    countryId: Int,
    modifier: Modifier = Modifier,
    viewModel: CountryDetailsViewModel = hiltViewModel<CountryDetailsViewModel, CountryDetailsViewModel.CountryDetailsFactory> { factory ->
        factory.create(countryId)
    },
) {
    val country = viewModel.country.collectAsState().value

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = dimensionResource(R.dimen.padding_medium)),
        contentAlignment = Alignment.Center
    ) {
        when (country) {
            Result.Loading -> {
                CircularProgressIndicator()
            }

            is Result.Success -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium))
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = country.data.name,
                            style = MaterialTheme.typography.displaySmall,
                            maxLines = 3,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = dimensionResource(R.dimen.padding_small))
                        )
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(country.data.flagUrl)
                                .crossfade(true)
                                .error(R.drawable.ic_image_placeholder)
                                .fallback(R.drawable.ic_image_placeholder)
                                .placeholder(R.drawable.ic_image_placeholder)
                                .build(),
                            contentDescription = stringResource(
                                R.string.image_flag,
                                country.data.name
                            ),
                            modifier = Modifier.size(dimensionResource(R.dimen.image_big_size))
                        )
                    }
                    Spacer(modifier = Modifier.size(0.dp))

                    InfoTextInput(title = stringResource(R.string.title_continent), innerBody = {
                        Text(country.data.region)
                    })
                    InfoTextInput(title = stringResource(R.string.title_capitals), innerBody = {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(
                                dimensionResource(R.dimen.padding_small)
                            )
                        ) {
                            country.data.capitals.map { capital ->
                                Text(capital)
                            }
                        }
                    })
                    InfoTextInput(title = stringResource(R.string.title_languages), innerBody = {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(
                                dimensionResource(R.dimen.padding_small)
                            )
                        ) {
                            country.data.languages.map { language ->
                                Text(language)
                            }
                        }
                    })
                    InfoTextInput(title = stringResource(R.string.title_currencies), innerBody = {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(
                                dimensionResource(R.dimen.padding_small)
                            )
                        ) {
                            country.data.currencies.map { currency ->
                                Text(
                                    stringResource(
                                        R.string.currency,
                                        currency.name,
                                        currency.symbol
                                    )
                                )
                            }
                        }
                    })

                }

            }

            else -> {
                Text(
                    text = stringResource(R.string.error_unknown),
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }

}
