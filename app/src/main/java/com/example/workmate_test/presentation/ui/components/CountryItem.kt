package com.example.workmate_test.presentation.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.request.error
import coil3.request.fallback
import coil3.request.placeholder
import com.example.workmate_test.R
import com.example.workmate_test.domain.models.Country

@Composable
fun CountryItem(country: Country, onClick: (Country) -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = { onClick(country) })
    )
    {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(
                all = dimensionResource(R.dimen.padding_medium),
            )
        ) {

            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(country.flagUrl)
                    .crossfade(true)
                    .error(R.drawable.ic_image_placeholder)
                    .fallback(R.drawable.ic_image_placeholder)
                    .placeholder(R.drawable.ic_image_placeholder)
                    .build(),
                contentDescription = stringResource(R.string.image_flag, country.name),
                modifier = Modifier.size(dimensionResource(R.dimen.image_default_size))
            )
            Spacer(modifier = Modifier.width(dimensionResource(R.dimen.padding_medium)))
            Text(text = country.name, color = MaterialTheme.colorScheme.onPrimaryContainer)

        }
        HorizontalDivider(
            thickness = dimensionResource(R.dimen.divider_thickness),
            color = Color.Gray.copy(alpha = 0.5f)
        )
    }
}