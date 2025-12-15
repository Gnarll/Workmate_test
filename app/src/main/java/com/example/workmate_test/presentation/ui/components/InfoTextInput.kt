package com.example.workmate_test.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.example.workmate_test.R

@Composable
fun InfoTextInput(
    title: String,
    innerBody: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    titleColor: Color = Color(0xFF2196F3),
    backgroundColor: Color = MaterialTheme.colorScheme.primaryContainer,
    cornerRadius: Dp = dimensionResource(R.dimen.corner_radius),
    padding: PaddingValues = PaddingValues(
        horizontal = dimensionResource(R.dimen.padding_medium),
        vertical = dimensionResource(
            R.dimen.padding_large
        )
    )
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = title,
            color = titleColor,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = dimensionResource(R.dimen.padding_x_small))
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(cornerRadius))
                .background(
                    color = backgroundColor,
                    shape = RoundedCornerShape(cornerRadius)
                )
                .padding(padding)
        ) {
            innerBody()
        }
    }
}

@Preview
@Composable
fun InfoTextInputPreview(modifier: Modifier = Modifier) {

    InfoTextInput(title = "Title", innerBody = { Text("Inner text") })
}