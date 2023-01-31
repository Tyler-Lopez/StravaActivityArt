package com.activityartapp.presentation.editArtScreen.composables

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.RadioButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.outlined.HelpOutline
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.activityartapp.presentation.common.type.Subhead
import com.activityartapp.presentation.editArtScreen.EditArtViewEvent
import com.activityartapp.presentation.ui.theme.Pumpkin
import com.activityartapp.presentation.ui.theme.StravaOrange
import com.activityartapp.presentation.ui.theme.spacing

@Composable
fun RadioButtonWithContent(
    isSelected: Boolean,
    text: String,
    content: @Composable ColumnScope.() -> Unit = {},
    onHelpPressed: (() -> Unit)? = null,
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
        Row(
            horizontalArrangement = Arrangement.spacedBy(spacing.small),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.defaultMinSize(minHeight = heightRadioButton),
                verticalArrangement = Arrangement.spacedBy(
                    spacing.medium,
                    Alignment.CenterVertically
                )
            ) {
                Subhead(text = text)
                content()
            }
            onHelpPressed?.let {
                IconButton(onClick = it) {
                    Icon(
                        imageVector = Icons.Default.HelpOutline,
                        contentDescription = null,
                        tint = Pumpkin
                    )
                }
            }
        }
    }
}