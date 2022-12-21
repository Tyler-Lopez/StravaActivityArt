package com.company.activityart.presentation.saveArtScreen

import android.Manifest
import android.graphics.Bitmap
import android.os.Build
import android.util.Size
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.company.activityart.BuildConfig
import com.company.activityart.architecture.EventReceiver
import com.company.activityart.presentation.common.button.ButtonSize
import com.company.activityart.presentation.common.button.HighEmphasisButton
import com.company.activityart.presentation.common.button.MediumEmphasisButton
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.launch

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun SaveArtStandby(
    bitmapScreenSize: Bitmap,
    buttonsEnabled: Boolean,
    downloadInProgress: Boolean,
    eventReceiver: EventReceiver<SaveArtViewEvent>,
    snackbarHostState: SnackbarHostState
) {
    val osGreaterThan10 = BuildConfig.VERSION_CODE > Build.VERSION_CODES.Q
    val permissionState = rememberPermissionState(Manifest.permission.WRITE_EXTERNAL_STORAGE)

    Box {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(
                space = 16.dp,
                alignment = Alignment.CenterVertically
            )
        ) {
            Image(
                bitmap = bitmapScreenSize.asImageBitmap(),
                modifier = Modifier.weight(1f, false),
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
                                eventReceiver.onEventDebounced(SaveArtViewEvent.ClickedDownload)
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
                ) { eventReceiver.onEventDebounced(SaveArtViewEvent.ClickedShare) }
            }
        }
        SnackbarHost(
            modifier = Modifier
                .align(Alignment.BottomCenter),
            hostState = snackbarHostState
        )
    }
}