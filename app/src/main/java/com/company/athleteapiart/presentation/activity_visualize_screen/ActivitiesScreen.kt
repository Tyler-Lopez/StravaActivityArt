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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavController
import com.company.athleteapiart.presentation.activity_select_screen.composable.ComposableReturnButton
import com.company.athleteapiart.presentation.activity_select_screen.composable.ComposableTopBar
import com.company.athleteapiart.ui.theme.*
import com.company.athleteapiart.util.isPermaDenied
import com.company.athleteapiart.util.saveImage
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ActivitiesScreen(
    navController: NavController,
    viewModel: ActivitiesVisualizeViewModel = hiltViewModel()
) {
    var activities by remember { viewModel.activities }
    val endReached by remember { viewModel.endReached }
    val loadError by remember { viewModel.loadError }
    val isLoading by remember { viewModel.isLoading }


    val context = LocalContext.current

    Scaffold(
        topBar = {
            ComposableTopBar {
                ComposableReturnButton(navController = navController)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        colors = ButtonDefaults.buttonColors(StravaOrange),
                        elevation = ButtonDefaults.elevation(0.dp),
                        onClick = {
                            saveImage(
                                bitmap = activitiesVisualizeCanvas(
                                    maxWidth = 3420,
                                    activities = activities
                                ),
                                context = context,
                                folderName = "ActivityVisualizer"
                            )
                        }
                    ) {

                        Text(
                            text = "Save",
                            fontFamily = Roboto,
                            fontSize = 20.sp,
                            color = White,
                            fontWeight = FontWeight.Medium
                        )
                        Icon(
                            imageVector = Icons.Default.Save,
                            contentDescription = "",
                            tint = White,
                            modifier = Modifier.padding(start = 5.dp)
                        )
                    }
                }
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
                                    .width(360.dp)
                                    .background(color = WarmGrey30)
                                    .drawBehind {
                                        val drawScopeWidth = this.size.width
                                        val drawScopeHeight = this.size.height

                                        for (i in 0..(drawScopeWidth
                                            .div(50f)
                                            .toInt())) {
                                            drawLine(
                                                color = WarmGrey40,
                                                start = Offset(
                                                    x = i * 50f,
                                                    y = 0f
                                                ),
                                                end = Offset(
                                                    x = i * 50f,
                                                    y = drawScopeHeight
                                                ),
                                                strokeWidth = 5f
                                            )
                                        }

                                        for (i in 0..(drawScopeHeight
                                            .div(50f)
                                            .toInt())) {
                                            drawLine(
                                                color = WarmGrey40,
                                                start = Offset(
                                                    y = i * 50f,
                                                    x = 0f
                                                ),
                                                end = Offset(
                                                    y = i * 50f,
                                                    x = drawScopeWidth
                                                ),
                                                strokeWidth = 5f
                                            )
                                        }
                                    },
                                verticalArrangement = Arrangement.Center
                            ) {
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
                                            activities = activities
                                        )
                                    )
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