package com.company.athleteapiart.presentation.activity_visualize_screen

import android.Manifest
import androidx.compose.runtime.getValue
import android.graphics.Bitmap
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.company.athleteapiart.data.remote.responses.ActivityDetailed
import android.annotation.SuppressLint
import androidx.compose.material.Button
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.company.athleteapiart.data.remote.responses.Activity
import com.company.athleteapiart.util.AthleteActivities.activities
import com.company.athleteapiart.util.isPermaDenied
import com.company.athleteapiart.util.saveImage
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState


@Composable
fun ActivitiesScreen(
    viewModel: ActivitiesVisualizeViewModel = hiltViewModel()
) {
    var activities by remember { viewModel.activities }
    val endReached by remember { viewModel.endReached }
    val loadError by remember { viewModel.loadError }
    val isLoading by remember { viewModel.isLoading }

    Column(modifier = Modifier.fillMaxSize()) {
        if (isLoading) {
            Text("Visualizing activities...")
        } else {
            ActivitiesDrawing(activities, viewModel)
        }
    }

}


@SuppressLint("InlinedApi")
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ActivitiesDrawing(
    activity: List<Activity>,
    viewModel: ActivitiesVisualizeViewModel
) {
    val bitmap = viewModel.onBitmapGenerated.observeAsState().value
    val context = LocalContext.current

    val permissionState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
        )
    )
    Button(onClick = {
        if (bitmap != null) {
            permissionState.permissions.forEach { perm ->
                when {
                    perm.hasPermission -> {
                        saveImage(
                            bitmap = bitmap,
                            context = context,
                            folderName = "ActivityVisualizer"
                        )
                    }
                    perm.shouldShowRationale -> {
                    }
                    perm.isPermaDenied() -> {

                    }
                    else -> {

                    }
                }
            }

        }
    }) {

        Text(
            text = "Save Image"
        )
    }
    AndroidView(
        factory = { context ->
            val activityVisualizeView = ActivityVisualizeView(
                ctx = context,
                activities = activities.value
            ) {
                viewModel.bitmapCreated(it)
            }
            activityVisualizeView
        }
    )

    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(
        key1 = lifecycleOwner,
        effect = {
            val observer = LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_START) {
                    permissionState.launchMultiplePermissionRequest()
                }
            }
            lifecycleOwner.lifecycle.addObserver(observer)

            onDispose {
                lifecycleOwner.lifecycle.removeObserver(observer)
            }
        }
    )

}
