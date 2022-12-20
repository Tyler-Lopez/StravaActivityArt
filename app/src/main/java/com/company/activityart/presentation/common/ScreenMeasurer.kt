package com.company.activityart.presentation.common

import android.util.Size
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import com.company.activityart.presentation.editArtScreen.EditArtViewEvent

@Composable
fun ScreenMeasurer(onMeasured: (Size) -> Unit) {
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        LocalDensity.current.run {
            SideEffect {
                onMeasured(
                    Size(
                        maxWidth.toPx().toInt(),
                        maxHeight.toPx().toInt()
                    )
                )
            }
        }
    }
}