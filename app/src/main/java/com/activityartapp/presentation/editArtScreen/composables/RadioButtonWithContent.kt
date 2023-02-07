package com.activityartapp.presentation.editArtScreen.composables

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.activityartapp.presentation.common.layout.ColumnSmallSpacing
import com.activityartapp.presentation.ui.theme.spacing

@Composable
fun RadioButtonWithContent(
    isSelected: Boolean,
    text: String,
    subtext: String? = null,
    fontFamily: FontFamily? = null,
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
                ColumnSmallSpacing(horizontalAlignment = Alignment.Start) {
                    Text(
                        text = text,
                        fontFamily = fontFamily,
                        style = MaterialTheme.typography.body1
                    )
                    subtext?.let {
                        Text(
                            text = it,
                            fontFamily = fontFamily,
                            style = MaterialTheme.typography.caption
                        )
                    }
                }
                content()
            }
            onHelpPressed?.let {
                IconButton(onClick = it) {
                    Icon(
                        imageVector = Icons.Default.HelpOutline,
                        contentDescription = null,
                        //  tint = Pumpkin
                    )
                }
            }
        }
    }
}