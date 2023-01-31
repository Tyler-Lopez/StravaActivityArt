package com.activityartapp.presentation.editArtScreen.composables

import androidx.compose.foundation.layout.*
import androidx.compose.material.RadioButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.activityartapp.presentation.common.type.Subhead
import com.activityartapp.presentation.editArtScreen.EditArtViewEvent
import com.activityartapp.presentation.ui.theme.spacing

@Composable
fun RadioButtonWithContent(
    isSelected: Boolean,
    text: String,
    content: @Composable ColumnScope.() -> Unit = {},
    onSelected: () -> Unit = {}
) {
    var heightRadioButton by remember { mutableStateOf(0.dp) }
    val localDensity = LocalDensity.current

    Row(
        horizontalArrangement = Arrangement.spacedBy(spacing.medium),
        verticalAlignment = Alignment.Top
    ) {
        RadioButton(
            selected = isSelected,
            onClick = onSelected,
            modifier = Modifier
                .onGloballyPositioned {
                    heightRadioButton = localDensity.run { it.size.height.toDp() }
                }
        )
        Column(
            modifier = Modifier.defaultMinSize(minHeight = heightRadioButton),
            verticalArrangement = Arrangement.spacedBy(spacing.medium, Alignment.CenterVertically)
        ) {
            Subhead(text = text)
            content()
        }
    }
}