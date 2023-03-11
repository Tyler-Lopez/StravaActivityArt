package com.activityartapp.presentation.common

import android.util.Size
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity

@Composable
fun ScreenMeasurer(onMeasured: (Size) -> Unit) {
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val localDensity = LocalDensity.current
        SideEffect {
            onMeasured(localDensity.run {
                Size(maxWidth.toPx().toInt(), maxHeight.toPx().toInt())
            })
        }
    }
}