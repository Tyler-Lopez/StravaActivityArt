package com.company.athleteapiart.presentation.time_select_screen

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
import com.company.athleteapiart.presentation.activity_visualize_screen.ActivitiesVisualizeViewModel
import com.company.athleteapiart.presentation.activity_visualize_screen.activitiesVisualizeCanvas
import com.company.athleteapiart.ui.theme.*
import com.company.athleteapiart.util.AthleteActivities
import com.company.athleteapiart.util.TimeUtils
import com.company.athleteapiart.util.isPermaDenied
import com.company.athleteapiart.util.saveImage
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun TimeSelectScreen(
    navController: NavController,
    viewModel: TimeSelectViewModel = hiltViewModel()
) {
    val endReached by remember { viewModel.endReached }
    val loadError by remember { viewModel.loadError }
    val isLoading by remember { viewModel.isLoading }
    val activitiesSize by remember { viewModel.activitiesSize }
    Scaffold(
        topBar = {
            ComposableTopBar {
                ComposableReturnButton(navController = navController)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        text = "Time Select",
                        fontFamily = Roboto,
                        fontSize = 20.sp,
                        color = White,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        },
        content = {

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (endReached) {
                    AthleteActivities.selectedActivities.value = AthleteActivities.activities.value
                    Text("${AthleteActivities.selectedActivities.value.size} activities loaded")
                    Button(onClick = {
                        navController.navigate("activity_screen")
                    }) {
                        Text("draw")


                    }
                }
                else if (isLoading) {
                    Text("Loading Activities ${activitiesSize}")
                } else if (loadError != "") {
                    Text("$loadError error")
                    Text("activities size is ${AthleteActivities.activities.value.size}")
                } else {
                    for (year in TimeUtils.yearsAvailable()) {
                        Button(onClick = {
                            viewModel.loadActivitiesByYear(year)
                        }) {
                            Text("$year")
                        }
                    }

                }
            }
        }
    )
}