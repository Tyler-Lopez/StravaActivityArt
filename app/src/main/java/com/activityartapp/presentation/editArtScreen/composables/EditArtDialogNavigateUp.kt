package com.activityartapp.presentation.editArtScreen.composables

import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.activityartapp.R
import com.activityartapp.architecture.EventReceiver
import com.activityartapp.presentation.common.button.Button
import com.activityartapp.presentation.common.button.ButtonEmphasis
import com.activityartapp.presentation.common.button.ButtonSize
import com.activityartapp.presentation.editArtScreen.EditArtViewEvent
import com.activityartapp.presentation.editArtScreen.EditArtViewEvent.DialogNavigateUpConfirmed
import com.activityartapp.presentation.ui.theme.spacing

@Composable
fun EditArtDialogNavigateUp(eventReceiver: EventReceiver<EditArtViewEvent>) {
    Dialog(
        onDismissRequest = {
            eventReceiver.onEvent(EditArtViewEvent.DialogDismissed)
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
                    Text(
                        text = stringResource(R.string.edit_art_dialog_exit_confirmation_prompt),
                        style = MaterialTheme.typography.body1
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Button(
                            emphasis = ButtonEmphasis.LOW,
                            modifier = Modifier,
                            size = ButtonSize.SMALL,
                            text = stringResource(R.string.edit_art_dialog_exit_confirmation_no)
                        ) { eventReceiver.onEvent(EditArtViewEvent.DialogDismissed) }
                        Spacer(modifier = Modifier.width(spacing.medium))
                        Button(
                            emphasis = ButtonEmphasis.LOW,
                            size = ButtonSize.SMALL,
                            text = stringResource(R.string.edit_art_dialog_exit_confirmation_yes)
                        ) { eventReceiver.onEvent(DialogNavigateUpConfirmed) }
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