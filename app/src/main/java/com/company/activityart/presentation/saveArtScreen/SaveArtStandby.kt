package com.company.activityart.presentation.saveArtScreen

import android.Manifest
import android.R.attr.bitmap
import android.content.Intent
import android.content.Intent.*
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.FileProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.company.activityart.presentation.saveArtScreen.SaveArtViewEvent.*
import com.company.activityart.BuildConfig
import com.company.activityart.R
import com.company.activityart.architecture.EventReceiver
import com.company.activityart.presentation.common.button.ButtonSize
import com.company.activityart.presentation.common.button.HighEmphasisButton
import com.company.activityart.presentation.common.button.MediumEmphasisButton
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import java.io.File
import java.io.FileOutputStream


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
    val osGreaterThan10 = BuildConfig.VERSION_CODE > Build.VERSION_CODES.Q
    val permissionState = rememberPermissionState(Manifest.permission.WRITE_EXTERNAL_STORAGE)

    /** Activity resumes upon returning to foreground when share activity is closed **/
    OnLifecycleEvent { _, event ->
        if (event == Lifecycle.Event.ON_RESUME) {
            eventReceiver.onEvent(ActivityResumed)
        }
    }

    Box {
        Column(
            modifier = Modifier.fillMaxSize(),
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
                    text = stringResource(R.string.save_art_download_art)
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
                                // TODO
                                println("Permission is perma-denied. Handle this later.")
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