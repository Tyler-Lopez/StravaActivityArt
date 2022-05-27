package com.company.athleteapiart.presentation.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun DropdownComposable(
    menuItems: List<String>,
    menuExpandedState: Boolean,
    onItemSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    defaultSelectedIndex: Int = 0,
) {

}