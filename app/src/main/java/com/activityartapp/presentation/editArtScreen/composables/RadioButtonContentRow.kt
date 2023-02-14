package com.activityartapp.presentation.editArtScreen.composables

import android.widget.ImageButton
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Flip
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.RotateRight
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.activityartapp.presentation.common.layout.ColumnSmallSpacing
import com.activityartapp.presentation.ui.theme.spacing

@Composable
fun RadioButtonContentRow(
    isSelected: Boolean,
    text: String,
    actionButtonContent: (@Composable () -> Unit)? = null,
    subtext: String? = null,
    fontFamily: FontFamily? = null,
    content: @Composable (ColumnScope.() -> Unit)? = null,
    onActionButtonPressed: (() -> Unit)? = null,
    onHelpPressed: (() -> Unit)? = null,
    onSelected: () -> Unit = {}
) {
    Icons.Default.Flip
    var heightRadioButton by remember { mutableStateOf(0.dp) }
    val localDensity = LocalDensity.current

    Row(
        horizontalArrangement = Arrangement.spacedBy(spacing.medium, Alignment.Start),
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
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column(
                modifier = Modifier.defaultMinSize(minHeight = heightRadioButton),
                verticalArrangement = Arrangement.spacedBy(
                    spacing.medium,
                    Alignment.CenterVertically
                )
            ) {
                ColumnSmallSpacing(horizontalAlignment = Alignment.Start) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(spacing.small),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = text,
                            fontFamily = fontFamily,
                            style = MaterialTheme.typography.body1
                        )
                        onHelpPressed?.let {
                            IconButton(onClick = it) {
                                Icon(
                                    imageVector = Icons.Default.HelpOutline,
                                    contentDescription = null
                                )
                            }
                        }
                    }
                    subtext?.let {
                        Text(
                            text = it,
                            fontFamily = fontFamily,
                            style = MaterialTheme.typography.caption
                        )
                    }
                }
                content?.let { it() }
            }
            if (actionButtonContent != null && onActionButtonPressed != null) {
                IconButton(
                    content = actionButtonContent,
                    onClick = onActionButtonPressed
                )
            }
        }
    }
}