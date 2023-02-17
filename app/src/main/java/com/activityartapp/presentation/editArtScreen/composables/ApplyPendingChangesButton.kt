package com.activityartapp.presentation.editArtScreen.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.activityartapp.R
import com.activityartapp.presentation.common.button.Button
import com.activityartapp.presentation.common.button.ButtonEmphasis
import com.activityartapp.presentation.common.button.ButtonSize
import com.activityartapp.presentation.ui.theme.spacing

@Composable
fun ApplyPendingChangesButton(
    prompt: String,
    onClickedApply: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(spacing.small, Alignment.Start),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = prompt,
            modifier = Modifier.weight(1f, false),
            style = MaterialTheme.typography.caption
        )
        Button(
            emphasis = ButtonEmphasis.MEDIUM,
            size = ButtonSize.SMALL,
            text = stringResource(R.string.edit_art_filters_distance_pending_change_prompt_accept),
            onClick = onClickedApply
        )
    }
}