package com.company.athleteapiart.presentation.composable

import androidx.compose.foundation.layout.*
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.company.athleteapiart.ui.theme.StravaOrange

@Composable
fun ComposableTopBar(
    leftContent: @Composable (RowScope.() -> Unit)?,
    rightContent: @Composable (RowScope.() -> Unit)?
) {
    TopAppBar(
        backgroundColor = StravaOrange,
        modifier = Modifier.height(65.dp)
    ) {
        if (leftContent != null) leftContent()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.End
        ) {
            if (rightContent != null) rightContent()
        }
    }
}