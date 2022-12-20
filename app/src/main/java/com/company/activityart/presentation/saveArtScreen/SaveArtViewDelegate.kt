package com.company.activityart.presentation.saveArtScreen

import android.Manifest
import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import com.company.activityart.presentation.saveArtScreen.SaveArtViewState.*
import androidx.compose.ui.res.stringResource
import com.company.activityart.BuildConfig
import com.company.activityart.R
import com.company.activityart.presentation.saveArtScreen.SaveArtViewEvent.*
import com.company.activityart.presentation.common.AppBarScaffold
import com.company.activityart.presentation.common.ScreenMeasurer
import com.company.activityart.presentation.common.button.ButtonSize
import com.company.activityart.presentation.common.button.HighEmphasisButton
import com.company.activityart.presentation.common.button.MediumEmphasisButton
import com.company.activityart.presentation.editArtScreen.EditArtViewEvent
import com.company.activityart.presentation.editArtScreen.subscreens.filters.composables.ScreenBackground
import com.company.activityart.presentation.saveArtScreen.SaveArtViewEvent.ClickedNavigateUp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun SaveArtViewDelegate(viewModel: SaveArtViewModel) {
    AppBarScaffold(
        text = stringResource(id = R.string.save_art_header),
        onNavigateUp = { viewModel.onEventDebounced(ClickedNavigateUp) },
    ) {
        val osGreaterThan10 = BuildConfig.VERSION_CODE > Build.VERSION_CODES.Q
        val permissionState = rememberPermissionState(Manifest.permission.WRITE_EXTERNAL_STORAGE)

        ScreenBackground {
            viewModel.viewState.collectAsState().value.apply {
                when (this) {
                    is Loading -> CircularProgressIndicator()
                    is Standby -> {
                        Image(
                            bitmap = bitmapScreenSize.asImageBitmap(),
                            contentDescription = null,
                            contentScale = ContentScale.Fit
                        )
                        Column {

                            HighEmphasisButton(
                                enabled = buttonsEnabled,
                                isLoading = downloadInProgress,
                                size = ButtonSize.MEDIUM,
                                text = "Download"
                            ) {
                                /** For Android <= 10, permission must be requested **/
                                permissionState.apply {
                                    when {
                                        osGreaterThan10 || hasPermission -> {
                                            viewModel.onEventDebounced(ClickedDownload)
                                        }
                                        !permissionRequested || shouldShowRationale -> {
                                            permissionState.launchPermissionRequest()
                                        }
                                        else -> {
                                            println("perma denied")
                                        }
                                    }
                                }
                            }
                            MediumEmphasisButton(
                                enabled = buttonsEnabled,
                                size = ButtonSize.MEDIUM,
                                text = "Share"
                            ) {
                                viewModel.onEventDebounced(ClickedShare)
                            }
                        }
                    }
                    null -> {
                        ScreenMeasurer { viewModel.onEvent(ScreenMeasured(it)) }
                    }
                }
            }
        }
    }
}