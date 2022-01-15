package com.company.athleteapiart.presentation.activity_visualize_screen

import android.Manifest
import androidx.compose.runtime.getValue
import android.graphics.Bitmap
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.company.athleteapiart.data.remote.responses.ActivityDetailed
import android.annotation.SuppressLint
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavController
import com.company.athleteapiart.data.remote.responses.Activity
import com.company.athleteapiart.presentation.activity_select_screen.composable.ComposableReturnButton
import com.company.athleteapiart.presentation.activity_select_screen.composable.ComposableTopBar
import com.company.athleteapiart.ui.theme.StravaOrange
import com.company.athleteapiart.util.AthleteActivities
import com.company.athleteapiart.util.AthleteActivities.activities
import com.company.athleteapiart.util.Constants.APP_NAME
import com.company.athleteapiart.util.isPermaDenied
import com.company.athleteapiart.util.saveImage
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState


@Composable
fun ActivitiesScreen(
    navController: NavController,
    viewModel: ActivitiesVisualizeViewModel = hiltViewModel()
) {
    var activities by remember { viewModel.activities }
    val endReached by remember { viewModel.endReached }
    val loadError by remember { viewModel.loadError }
    val isLoading by remember { viewModel.isLoading }

    Scaffold(
        topBar = {
            ComposableTopBar {
                ComposableReturnButton(navController = navController)
            }
        },
        content = {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (isLoading) {
                    Text("Applying filters...")
                } else {
                    ActivitiesDrawing(activities, viewModel)
                }
            }
        }
    )


}


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ActivitiesDrawing(
    activities: List<Activity>,
    viewModel: ActivitiesVisualizeViewModel
) {
    val bitmap = viewModel.onBitmapGenerated.observeAsState().value
    val context = LocalContext.current


    val permissionState = rememberPermissionState(
        permission = Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(
        key1 = lifecycleOwner,
        effect = {
            val observer = LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_START) {
                    permissionState.launchPermissionRequest()
                }
            }
            lifecycleOwner.lifecycle.addObserver(observer)

            onDispose {
                lifecycleOwner.lifecycle.removeObserver(observer)
            }
        }
    )

    when {
        permissionState.hasPermission -> {
            AndroidView(
                factory = { context ->
                    val activityVisualizeView = ActivitiesVisualizeView(
                        ctx = context,
                        activities = activities,
                    ) {
                        viewModel.bitmapCreated(it)
                    }
                    activityVisualizeView
                }
            )
        }
        permissionState.shouldShowRationale -> {
            Text(
                text = "$APP_NAME requires access to store files in your directory so that you may save your visualization.",
                modifier = Modifier
                    .fillMaxSize()
            )
        }
        permissionState.isPermaDenied() -> {
            Text(
                text = "$APP_NAME has been denied access to store files in your directory." +
                        " The save functionality will not work without it." +
                        " To change this, please open your Settings, find this app, and enable permissions manually.",
                modifier = Modifier
                    .fillMaxSize()
            )
        }
    }
}


/*
= {
                            if (bitmap != null) {
                                saveImage(
                                    bitmap = bitmap,
                                    context = context,
                                    folderName = "ActivityVisualizer"
                                )
                            }
                        }
 */