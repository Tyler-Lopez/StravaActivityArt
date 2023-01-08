package com.company.activityart.presentation.saveArtScreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.company.activityart.R
import com.company.activityart.presentation.common.AppBarScaffold
import com.company.activityart.presentation.common.ScreenMeasurer
import com.company.activityart.presentation.saveArtScreen.SaveArtViewEvent.ClickedNavigateUp
import com.company.activityart.presentation.saveArtScreen.SaveArtViewEvent.ScreenMeasured
import com.company.activityart.presentation.saveArtScreen.SaveArtViewState.Loading
import com.company.activityart.presentation.saveArtScreen.SaveArtViewState.Standby

@Composable
fun SaveArtViewDelegate(viewModel: SaveArtViewModel) {
    AppBarScaffold(
        text = stringResource(id = R.string.save_art_header),
        onNavigateUp = { viewModel.onEventDebounced(ClickedNavigateUp) },
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            viewModel.viewState.collectAsState().value.apply {
                when (this) {
                    is Loading -> CircularProgressIndicator()
                    is Standby -> SaveArtStandby(
                        bitmapScreenSize = bitmapScreenSize,
                        buttonsEnabled = buttonsEnabled,
                        downloadInProgress = downloadInProgress,
                        shareInProgress = shareInProgress,
                        eventReceiver = viewModel,
                        snackbarHostState = snackbarHostState
                    )
                    null -> ScreenMeasurer { viewModel.onEvent(ScreenMeasured(it)) }
                }
            }
        }
    }
}