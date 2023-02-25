package com.activityartapp.presentation.editArtScreen.composables

import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.activityartapp.R
import com.activityartapp.architecture.EventReceiver
import com.activityartapp.presentation.common.button.Button
import com.activityartapp.presentation.common.button.ButtonEmphasis
import com.activityartapp.presentation.common.button.ButtonSize
import com.activityartapp.presentation.editArtScreen.EditArtDialogType
import com.activityartapp.presentation.editArtScreen.EditArtViewEvent
import com.activityartapp.presentation.editArtScreen.EditArtViewEvent.*
import com.activityartapp.presentation.ui.theme.spacing

@Composable
fun EditArtDialogInfo(
    body: Array<String>,
    type: EditArtDialogType,
    eventReceiver: EventReceiver<EditArtViewEvent>
) {
    Dialog(
        onDismissRequest = {
            eventReceiver.onEvent(DialogDismissed)
        },
        content = {
            Card {
                Column(
                    modifier = Modifier.padding(
                        start = spacing.medium,
                        end = spacing.medium,
                        top = spacing.medium,
                        bottom = spacing.small
                    ),
                    verticalArrangement = Arrangement.spacedBy(spacing.medium)
                ) {
                    body.forEach {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.body1
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(
                            spacing.medium,
                            Alignment.End
                        )
                    ) {
                        when (type) {
                            EditArtDialogType.DISMISS_BY_OK ->
                                TextButton(onClick = { eventReceiver.onEvent(DialogDismissed) }) {
                                    Text(
                                        text = stringResource(R.string.edit_art_dialog_info_dismiss),
                                        style = MaterialTheme.typography.button
                                    )
                                }
                            EditArtDialogType.REMOVE_AND_DISMISS_BY_CANCEL -> {
                                TextButton(onClick = { eventReceiver.onEvent(DialogDismissed) }) {
                                    Text(
                                        text = stringResource(R.string.edit_art_dialog_exit_remove_no),
                                        style = MaterialTheme.typography.button
                                    )
                                }
                                TextButton(onClick = { eventReceiver.onEvent(ArtMutatingEvent.StyleBackgroundColorRemoveConfirmed) }) {
                                    Text(
                                        text = stringResource(R.string.edit_art_dialog_exit_remove_yes),
                                        style = MaterialTheme.typography.button
                                    )
                                }
                            }
                            EditArtDialogType.DISCARD_AND_DISMISS_BY_CANCEL -> {
                                TextButton(onClick = { eventReceiver.onEvent(DialogDismissed) }) {
                                    Text(
                                        text = stringResource(R.string.edit_art_dialog_exit_confirmation_no),
                                        style = MaterialTheme.typography.button
                                    )
                                }
                                TextButton(onClick = { eventReceiver.onEvent(DialogNavigateUpConfirmed) }) {
                                    Text(
                                        text = stringResource(R.string.edit_art_dialog_exit_confirmation_yes),
                                        style = MaterialTheme.typography.button
                                    )
                                }
                            }
                        }
                    }
                }
            }
        },
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    )
}