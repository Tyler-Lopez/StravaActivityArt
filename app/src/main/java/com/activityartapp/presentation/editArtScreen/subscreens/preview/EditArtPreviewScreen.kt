package com.activityartapp.presentation.editArtScreen.subscreens.preview

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.activityartapp.R
import com.activityartapp.architecture.EventReceiver
import com.activityartapp.presentation.common.ScreenBackground
import com.activityartapp.presentation.common.button.ButtonSize
import com.activityartapp.presentation.common.button.LowEmphasisButton
import com.activityartapp.presentation.common.type.Subhead
import com.activityartapp.presentation.common.type.SubheadHeavy
import com.activityartapp.presentation.common.type.TitleTwo
import com.activityartapp.presentation.editArtScreen.EditArtViewEvent

@Composable
fun EditArtPreview(
    atLeastOneActivitySelected: Boolean,
    backgroundIsTransparent: Boolean,
    bitmap: Bitmap?,
    eventReceiver: EventReceiver<EditArtViewEvent>
) {
    ScreenBackground {

        if (!atLeastOneActivitySelected) {
            TitleTwo(text = stringResource(R.string.edit_art_preview_activities_zero_count_header))
            Subhead(text = stringResource(R.string.edit_art_preview_activities_zero_count_description))

        } else {
            bitmap?.let {
                Image(
                    bitmap = it.asImageBitmap(),
                    contentDescription = stringResource(R.string.edit_art_preview_image_content_description),
                    modifier = Modifier.weight(1f, false),
                    contentScale = ContentScale.Fit,
                )
                if (backgroundIsTransparent) {
                    LowEmphasisButton(
                        text = "Why is there a checkered pattern?",
                        size = ButtonSize.SMALL
                    ) { eventReceiver.onEvent(EditArtViewEvent.ClickedInfoCheckeredPattern) }
                }
            } ?: run {
                CircularProgressIndicator()
            }
        }
    }
}