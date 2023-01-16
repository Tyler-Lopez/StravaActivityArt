package com.activityartapp.presentation.common

import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import com.activityartapp.R
import com.activityartapp.presentation.ui.theme.spacing

@Composable
fun CardColumn(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        backgroundColor = colorResource(R.color.n20_icicle),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .defaultMinSize(minWidth = dimensionResource(id = R.dimen.button_min_width))
                .padding(16.dp),
            content = content,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(spacing.medium, Alignment.CenterVertically)
        )
    }
}