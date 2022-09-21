package com.company.activityart.presentation.edit_art_screen.subscreens.preview

import android.graphics.Bitmap
import androidx.annotation.Px
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.currentCompositionLocalContext
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import com.company.activityart.architecture.EventReceiver
import com.company.activityart.presentation.edit_art_screen.subscreens.preview.EditArtPreviewViewEvent.*

@Composable
fun EditArtPreviewStandby(bitmap: Bitmap?) {
    BoxWithConstraints(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        bitmap?.let {
            Image(
                bitmap = it.asImageBitmap(),
                contentDescription = null,
                contentScale = ContentScale.Fit
            )
        }
    }
}