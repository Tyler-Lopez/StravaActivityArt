package com.company.activityart.presentation.editArtScreen.subscreens.filters.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.RangeSlider
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import com.company.activityart.R
import com.company.activityart.presentation.editArtScreen.subscreens.filters.Section
import com.company.activityart.presentation.ui.theme.spacing

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FilterSectionDistances() {
    Section(
        header = stringResource(R.string.edit_art_filters_distance_header),
        description = stringResource(R.string.edit_art_filters_distance_description),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(spacing.medium)
        ) {
            TextField(
                "",
                modifier = Modifier.weight(1f),
                onValueChange = {},
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
            )
            RangeSlider(
                values = 0f..1f,
                modifier = Modifier.weight(2f, true),
                onValueChange = {},
                valueRange = 0f..1f
            )
            TextField(
                "",
                modifier = Modifier.weight(1f),
                onValueChange = {},
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
            )
        }

    }
}