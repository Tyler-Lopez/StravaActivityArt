package com.activityartapp.presentation.editArtScreen.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.RadioButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import com.activityartapp.presentation.common.type.Subhead
import com.activityartapp.presentation.ui.theme.spacing

@Composable
fun RadioButtonWithText(
    isSelected: Boolean,
    text: String,
    onSelected: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(spacing.medium),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = isSelected,
            onClick = onSelected
        )
        Subhead(text = text)
    }
}