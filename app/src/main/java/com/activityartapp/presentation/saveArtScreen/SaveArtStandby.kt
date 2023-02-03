package com.activityartapp.presentation.saveArtScreen

import android.Manifest
import android.graphics.Bitmap
import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.activityartapp.R
import com.activityartapp.architecture.EventReceiver
import com.activityartapp.presentation.common.ScreenBackground
import com.activityartapp.presentation.common.button.ButtonSize
import com.activityartapp.presentation.common.button.HighEmphasisButtonLegacy
import com.activityartapp.presentation.common.button.MediumEmphasisButtonLegacy
import com.activityartapp.presentation.saveArtScreen.SaveArtViewEvent.*
import com.activityartapp.presentation.saveArtScreen.SaveArtViewState.Standby.DownloadShareStatusType.*
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun SaveArtStandby(
    bitmapScreenSize: Bitmap,
    downloadShareStatusType: SaveArtViewState.Standby.DownloadShareStatusType,
    eventReceiver: EventReceiver<SaveArtViewEvent>
) {
    val snackbarHostState = remember { SnackbarHostState() }

    /** OS Greater than Android 10 (API 30) do not require permissions **/
    val osGreaterThan10 = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
    val permissionState = rememberPermissionState(Manifest.permission.WRITE_EXTERNAL_STORAGE)

    /** Unfortunate necessary hack with the permissions API.
     * See the section "Limitations" at
     * @link(https://google.github.io/accompanist/permissions)
     *
     * Because there is no way to differentiate before the first invocation of
     * [PermissionState.launchPermissionRequest] whether the first permission request
     * will not be successful due to the user having previously clicked "Do Not Ask Me Again".
     *
     * To get around this, we ask immediately rather than after Download is pressed. When
     * they click "Download" thereafter we know whether or not they are perma-denied. **/
    SideEffect {
        permissionState.apply {
            if (!osGreaterThan10 && (!permissionRequested || (permissionRequested && shouldShowRationale))) {
                launchPermissionRequest()
            }
        }
    }

    /** Activity resumes upon returning to foreground when share activity is closed **/
    OnLifecycleEvent { _, event ->
        if (event == Lifecycle.Event.ON_RESUME) {
            eventReceiver.onEvent(ActivityResumed)
        }
    }

    val statusIsDownloading = (downloadShareStatusType == DOWNLOAD_IN_PROGRESS)
        .or(downloadShareStatusType == DOWNLOAD_FAILURE)
        .or(downloadShareStatusType == DOWNLOAD_SUCCESS)
    val statusIsSharing = (downloadShareStatusType == SHARE_IN_PROGRESS)
        .or(downloadShareStatusType == SHARE_SUCCESS)
        .or(downloadShareStatusType == SHARE_WAITING_FOR_RETURN)
    val statusIsStandby = downloadShareStatusType == STANDBY

    val snackbarMessage = downloadShareStatusType.snackbarStringRes?.let { stringResource(it) }
    LaunchedEffect(downloadShareStatusType) {
        snackbarMessage?.let { snackbarHostState.showSnackbar(it) }
        when (downloadShareStatusType) {
            DOWNLOAD_FAILURE -> ReceivedDownloadFailure
            DOWNLOAD_SUCCESS -> ReceivedDownloadSuccess
            SHARE_FAILURE -> ReceivedShareFailure
            SHARE_SUCCESS -> ReceivedShareSuccess
            STANDBY, DOWNLOAD_IN_PROGRESS, SHARE_IN_PROGRESS, SHARE_WAITING_FOR_RETURN -> null
        }?.let { eventReceiver.onEvent(it) }
    }

    Box {
        ScreenBackground {
            Image(
                bitmap = bitmapScreenSize.asImageBitmap(),
                modifier = Modifier.weight(1f, false),
                contentDescription = stringResource(R.string.save_art_content_description_preview),
                contentScale = ContentScale.Fit
            )
            Column {
                HighEmphasisButtonLegacy(
                    enabled = statusIsStandby,
                    isLoading = statusIsDownloading,
                    size = ButtonSize.MEDIUM,
                    text = stringResource(R.string.save_art_download_art)
                ) {
                    permissionState.apply {
                        when {
                            /** Permission must be requested only when Android is <= 10 **/
                            osGreaterThan10 || hasPermission -> {
                                eventReceiver.onEventDebounced(ClickedDownload)
                            }
                            !permissionRequested || (permissionRequested && shouldShowRationale) -> {
                                launchPermissionRequest()
                            }
                            else -> eventReceiver.onEventDebounced(
                                ClickedDownloadWhenPermissionPermaDenied
                            )
                        }
                    }
                }
                MediumEmphasisButtonLegacy(
                    enabled = statusIsStandby,
                    size = ButtonSize.MEDIUM,
                    isLoading = statusIsSharing,
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
        onDispose { lifecycle.removeObserver(observer) }
    }
}