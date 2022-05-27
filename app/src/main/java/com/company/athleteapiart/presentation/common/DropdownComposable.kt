package com.company.athleteapiart.presentation.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun DropdownComposable(
    menuItems: List<String>,
    onItemSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    defaultSelectedIndex: Int = 0,
) {
    val expanded = remember { mutableStateOf(false) }
    val selectedIndex = remember { mutableStateOf(defaultSelectedIndex) }

    Box(modifier = modifier) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxSize()
                .clickable {
                    expanded.value = !expanded.value
                }
        ) {
            Text(
                text = menuItems.getOrElse(selectedIndex.value) { "ERR" }
            )

        }
        DropdownMenu(
            expanded = expanded.value,
            onDismissRequest = { expanded.value = false },
            modifier = Modifier.fillMaxSize()
        ) {
            menuItems.forEachIndexed { index, item ->
                DropdownMenuItem(onClick = {
                    selectedIndex.value = index
                    onItemSelected(index)
                }) {
                    Text(
                        text = item
                    )
                }
            }
        }
    }
}