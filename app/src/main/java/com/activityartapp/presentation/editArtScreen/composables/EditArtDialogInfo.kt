package com.activityartapp.presentation.editArtScreen.composables

import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.activityartapp.architecture.EventReceiver
import com.activityartapp.presentation.common.button.ButtonSize
import com.activityartapp.presentation.common.button.LowEmphasisButton
import com.activityartapp.presentation.common.type.Body
import com.activityartapp.presentation.editArtScreen.EditArtViewEvent
import com.activityartapp.presentation.editArtScreen.EditArtViewEvent.*
import com.activityartapp.presentation.ui.theme.spacing
import com.activityartapp.R

@Composable
fun EditArtDialogInfo(
    body: Array<String>,
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
                    body.forEach { Body(text = it) }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        LowEmphasisButton(
                            size = ButtonSize.SMALL,
                            modifier = Modifier,
                            text = stringResource(R.string.edit_art_dialog_info_dismiss)
                        ) { eventReceiver.onEvent(DialogDismissed) }
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