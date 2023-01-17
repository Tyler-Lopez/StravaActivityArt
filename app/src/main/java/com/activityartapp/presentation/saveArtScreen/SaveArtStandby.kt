package com.activityartapp.presentation.saveArtScreen

import android.Manifest
import android.content.Intent.*
import android.graphics.Bitmap
import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.activityartapp.presentation.saveArtScreen.SaveArtViewEvent.*
import com.activityartapp.BuildConfig
import com.activityartapp.R
import com.activityartapp.architecture.EventReceiver
import com.activityartapp.presentation.common.ScreenBackground
import com.activityartapp.presentation.common.button.ButtonSize
import com.activityartapp.presentation.common.button.HighEmphasisButton
import com.activityartapp.presentation.common.button.MediumEmphasisButton
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun SaveArtStandby(
    bitmapScreenSize: Bitmap,
    buttonsEnabled: Boolean,
    downloadInProgress: Boolean,
    shareInProgress: Boolean,
    eventReceiver: EventReceiver<SaveArtViewEvent>,
    snackbarHostState: SnackbarHostState
) {
    /** OS Greater than Android 10 (API 30) do not require permissions **/
    val osGreaterThan10 = Build.VERSION.SDK_INT > Build.VERSION_CODES.Q
    val permissionState = rememberPermissionState(Manifest.permission.WRITE_EXTERNAL_STORAGE)

    /** Activity resumes upon returning to foreground when share activity is closed **/
    OnLifecycleEvent { _, event ->
        if (event == Lifecycle.Event.ON_RESUME) {
            eventReceiver.onEvent(ActivityResumed)
        }
    }

    Box {
        ScreenBackground {
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
                    text = stringResource(R.string.save_art_download_art)
                ) {
                    /** For Android <= 10, permission must be requested **/
                    permissionState.apply {
                        when {
                            osGreaterThan10 || hasPermission -> {
                                eventReceiver.onEventDebounced(ClickedDownload)
                            }
                            shouldShowRationale -> {
                                permissionState.launchPermissionRequest()
                            }
                            else -> {
                                eventReceiver.onEventDebounced(
                                    ClickedDownloadWhenPermissionPermaDenied
                                )
                            }
                        }
                    }
                }
                MediumEmphasisButton(
                    enabled = buttonsEnabled,
                    size = ButtonSize.MEDIUM,
                    isLoading = shareInProgress,
                    text = stringResource(R.string.save_art_share_art)
                ) { eventReceiver.onEventDebounced(ClickedShare) }
            }
        }
        SnackbarHost(
            modifier = Modifier
                .align(Alignment.BottomCenter),
            hostState = snackbarHostState
        )
    }
}

@Composable
fun OnLifecycleEvent(onEvent: (owner: LifecycleOwner, event: Lifecycle.Event) -> Unit) {
    val eventHandler = rememberUpdatedState(onEvent)
    val lifecycleOwner = rememberUpdatedState(LocalLifecycleOwner.current)

    DisposableEffect(lifecycleOwner.value) {
        val lifecycle = lifecycleOwner.value.lifecycle
        val observer = LifecycleEventObserver { owner, event ->
            eventHandler.value(owner, event)
        }

        lifecycle.addObserver(observer)
        onDispose {
            lifecycle.removeObserver(observer)
        }
    }
}