package com.company.athleteapiart.presentation.visualize_screen

import android.graphics.Bitmap
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.company.athleteapiart.presentation.activity_visualize_screen.ActivityImageHandler
import com.company.athleteapiart.presentation.activity_visualize_screen.activitiesVisualizeCanvas
import com.company.athleteapiart.presentation.ui.theme.StravaOrange
import com.company.athleteapiart.presentation.visualize_screen.VisualizeScreenState.*

@Composable
fun VisualizeScreen(
    athleteId: Long,
    yearMonths: Array<Pair<Int, Int>>,
    navController: NavHostController,
    activityTypes: Array<String>? = null, // If null then do not filter by activityTypes
    gears: Array<String?>? = null, // If null do not filter, if string is null then that means null gearId is included
    distances: ClosedFloatingPointRange<Float>? = null,
    viewModel: VisualizeScreenViewModel = hiltViewModel()
) {

    val screenState by remember { viewModel.screenState }
    val context = LocalContext.current
    val bitmap by remember { viewModel.bitmap }

    when (screenState) {
        LAUNCH -> {
            viewModel.loadActivities(
                context = context,
                athleteId = athleteId,
                yearMonths = yearMonths,
                activityTypes = activityTypes,
                gears = gears,
                distances = distances
            )
        }
        LOADING -> {

        }
        PERMISSION_ACCEPTED -> {

        }
        STANDBY -> {
            BoxWithConstraints(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {

                AndroidView(
                    factory = {
                        // This view is set to View.GONE so this does not appear
                        VisualizeView(
                            ctx = it,
                            height = 1080,
                            width = 1920,
                            // However, on bitmap created the viewModel updates bitmap
                            onBitmapCreated = { bm ->
                                viewModel.updateBitmap(bm)
                            }
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(StravaOrange)
                            ) {}
                        }
                    }
                )

                // This is where the bitmap (Composable --> Bitmap --> Composable)
                // is presented to the user
                VisualizeImageHandler(bitmap = bitmap)


                /* {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(StravaOrange)
                ) {}
            }

                 */


                /*
                val maxHeight = this.maxHeight
                val maxWidth = this.maxWidth

                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    println(viewModel.activities.size.toString() + " is activities size")
                    ActivityImageHandler(
                        activitiesVisualizeCanvas(
                            maxWidth = LocalDensity.current.run {
                                maxWidth.toPx().toInt()
                            },
                            LocalContext.current,
                            activities = viewModel.activities
                        )
                    )
                }
            }

                 */
            }
        }

        /*
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
                                                    LocalContext.current,
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

         */
    }
}