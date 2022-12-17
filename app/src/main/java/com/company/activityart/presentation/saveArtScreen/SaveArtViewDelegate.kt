package com.company.activityart.presentation.saveArtScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import com.company.activityart.presentation.saveArtScreen.SaveArtViewState.*
import androidx.compose.ui.res.stringResource
import com.company.activityart.R
import com.company.activityart.presentation.common.AppBarScaffold
import com.company.activityart.presentation.common.button.ButtonSize
import com.company.activityart.presentation.common.button.HighEmphasisButton
import com.company.activityart.presentation.common.button.MediumEmphasisButton
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
                    is Standby -> {
                        Image(
                            bitmap = bitmap.asImageBitmap(),
                            contentDescription = null,
                            contentScale = ContentScale.Fit
                        )
                        Column {
                            HighEmphasisButton(
                                size = ButtonSize.MEDIUM,
                                text = "Download"
                            ) {
                            }
                            MediumEmphasisButton(
                                size = ButtonSize.MEDIUM,
                                text = "Share"
                            ) {

                            }
                        }
                    }
                }
            }
        }
    }
}