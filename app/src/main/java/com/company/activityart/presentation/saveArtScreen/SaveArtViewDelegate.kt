package com.company.activityart.presentation.saveArtScreen

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.company.activityart.R
import com.company.activityart.presentation.common.AppBarScaffold
import com.company.activityart.presentation.common.type.SubheadHeavy
import com.company.activityart.presentation.saveArtScreen.SaveArtViewEvent.*
import com.company.activityart.presentation.editArtScreen.subscreens.filters.composables.ScreenBackground
import com.company.activityart.presentation.ui.theme.White
import com.company.activityart.presentation.ui.theme.spacing

@Composable
fun SaveArtViewDelegate(viewModel: SaveArtViewModel) {
    AppBarScaffold(
        text = "Save Art", // todo str res
        onNavigateUp = { viewModel.onEventDebounced(ClickedNavigateUp) },
    ) {
        ScreenBackground {
            viewModel.viewState.collectAsState().value?.apply {
            }
        }
    }
}