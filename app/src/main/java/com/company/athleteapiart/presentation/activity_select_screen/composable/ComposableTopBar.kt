package com.company.athleteapiart.presentation.activity_select_screen.composable

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.height
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.company.athleteapiart.ui.theme.StravaOrange

@Composable
fun ComposableTopBar(
    content: @Composable RowScope.() -> Unit
) {
    TopAppBar(
        backgroundColor = StravaOrange,
        modifier = Modifier.height(65.dp),
        content = content
    )
}