package com.company.athleteapiart.presentation.visualize_screen

import android.graphics.Bitmap
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.drawToBitmap
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
            println("here in launch")
            SideEffect {
                viewModel.loadActivities(
                    context = context,
                    athleteId = athleteId,
                    yearMonths = yearMonths,
                    activityTypes = activityTypes,
                    gears = gears,
                    distances = distances
                )
            }
        }
        LOADING -> {

        }
        PERMISSION_ACCEPTED -> {

        }
        STANDBY -> {
            println("Here in standby")
            BoxWithConstraints(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                val maxWidth = this.maxWidth

                Text(
                    "Image and text below is a bitmap",
                    fontSize = 18.sp,
                    modifier = Modifier.padding(bottom = 20.dp)
                )

                // This is where the bitmap (Composable --> Bitmap --> Composable)
                // is presented to the user
                val snapShot = CaptureBitmap {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp)
                            .background(Color.LightGray)
                    ) {

                    }
                    //.. wite composable you want to capture
                    // it would be visible on view as well
                }

// Caution : needs to be done on click action
// ui must be visible/laid out before calling this
                val bitmap = snapShot.invoke()


                //    CatImageHandler()

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