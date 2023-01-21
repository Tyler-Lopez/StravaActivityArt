package com.activityartapp.presentation.editArtScreen.subscreens.preview

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import com.activityartapp.R
import com.activityartapp.presentation.common.ScreenBackground
import com.activityartapp.presentation.common.type.Subhead
import com.activityartapp.presentation.common.type.TitleTwo

@Composable
fun EditArtPreview(
    atLeastOneActivitySelected: Boolean,
    bitmap: Bitmap?
) {
    if (!atLeastOneActivitySelected) {
        ScreenBackground {
            TitleTwo(text = stringResource(R.string.edit_art_preview_activities_zero_count_header))
            Subhead(text = stringResource(R.string.edit_art_preview_activities_zero_count_description))
        }
    } else {
        bitmap?.let {
            Image(
                bitmap = it.asImageBitmap(),
                contentDescription = stringResource(R.string.edit_art_preview_image_content_description),
                contentScale = ContentScale.Fit
            )
        } ?: run {
            CircularProgressIndicator()
        }
    }
}