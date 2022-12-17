package com.company.activityart.presentation.saveArtScreen

import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.company.activityart.presentation.saveArtScreen.SaveArtViewState.*
import androidx.compose.ui.res.stringResource
import com.company.activityart.R
import com.company.activityart.presentation.common.AppBarScaffold
import com.company.activityart.presentation.editArtScreen.subscreens.filters.composables.ScreenBackground
import com.company.activityart.presentation.saveArtScreen.SaveArtViewEvent.ClickedNavigateUp

@Composable
fun SaveArtViewDelegate(viewModel: SaveArtViewModel) {
    AppBarScaffold(
        text = stringResource(id = R.string.save_art_header),
        onNavigateUp = { viewModel.onEventDebounced(ClickedNavigateUp) },
    ) {
        ScreenBackground {
            viewModel.viewState.collectAsState().value?.apply {
                when (this) {
                    is Loading -> CircularProgressIndicator()
                }
            }
        }
    }
}