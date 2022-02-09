package com.company.athleteapiart.presentation.activity_visualize_screen

import android.Manifest
import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.runtime.getValue
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.material.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavHostController
import com.company.athleteapiart.Screen
import com.company.athleteapiart.presentation.composable.*
import com.company.athleteapiart.ui.spacing
import com.company.athleteapiart.ui.theme.*
import com.company.athleteapiart.util.isPermaDenied
import com.company.athleteapiart.util.saveImage
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ActivitiesScreen(
    navController: NavHostController,
    viewModel: ActivitiesVisualizeViewModel = hiltViewModel()
) {
    var activities = viewModel.activities
    val endReached by remember { viewModel.endReached }
    val loadError by remember { viewModel.loadError }
    val isLoading by remember { viewModel.isLoading }


    val context = LocalContext.current

    Scaffold(
        topBar = {
            ComposableTopBar(
                leftContent = {
                    ComposableReturnButton(onClick = {
                        navController.navigateUp()
                    })
                },
                rightContent = {
                    ComposableSubtext(
                        text = "Image Preview",
                        color = Color.White
                    )
                    /*
                    ComposableSaveImageButton {
                        saveImage(
                            bitmap = activitiesVisualizeCanvas(
                                maxWidth = 3420,
                                activities = activities.value
                            ),
                            context = context,
                            folderName = "ActivityVisualizer"
                        )
                    }

                     */
                }
            )
        },
        content = {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,

                ) {
                if (isLoading) {
                    Text("Applying filters...")
                } else {
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
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(color = White),
                                //.padding(bottom = 75.dp),
                                verticalArrangement = Arrangement.Center
                            ) {
                                ComposableScreenWrapper(
                                    // Create room for large button
                                    //     modifier = Modifier.padding(bottom = 75.dp)
                                ) {
                                    ComposableShadowBox {
                                        Column {
                                            BoxWithConstraints(
                                                modifier = Modifier
                                                    .fillMaxSize(),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                val maxWidth = this.maxWidth
                                                ActivityImageHandler(
                                                    activitiesVisualizeCanvas(
                                                        maxWidth = LocalDensity.current.run {
                                                            maxWidth.toPx().toInt()
                                                        },
                                                        activities = activities.value
                                                    )
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        permissionState.shouldShowRationale -> {
                        }
                        permissionState.isPermaDenied() -> {
                        }
                    }

                }
            }
        },
        bottomBar = {
            ComposableLargeButton(
                text = "Save Image",
                onClick = {
                    navController.navigate(Screen.SaveImage.route)
                }
            )
        }
    )
}


@Composable
fun ActivityImageHandler(bitmap: Bitmap) {
    ActivityImage(bitmap = bitmap)
}

@Composable
fun ActivityImage(bitmap: Bitmap?) {
    if (bitmap != null) {
        Image(
            bitmap = bitmap.asImageBitmap(),
            contentDescription = null,
            contentScale = ContentScale.Fit
        )
    }
}