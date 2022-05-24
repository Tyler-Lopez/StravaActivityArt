package com.company.athleteapiart.presentation.visualize_screen

import android.graphics.Paint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavHostController
import com.company.athleteapiart.presentation.common.ButtonComposable
import com.company.athleteapiart.presentation.common.LoadingComposable
import com.company.athleteapiart.presentation.common.WarningComposable
import com.company.athleteapiart.presentation.ui.theme.*
import com.company.athleteapiart.presentation.visualize_screen.VisualizeScreenState.*
import com.company.athleteapiart.util.isPermaDenied
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
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
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(Icicle),
        contentAlignment = Alignment.Center
    ) {

        val maxWidth = this.maxWidth
        val maxHeight = this.maxHeight

        when (screenState) {
            LAUNCH -> {
                println("launch invoked")
                viewModel.loadActivities(
                    context = context,
                    athleteId = athleteId,
                    yearMonths = yearMonths,
                    activityTypes = activityTypes,
                    gears = gears,
                    distances = distances
                )
            }
            GET_SPECIFICATION -> {
                val width = LocalDensity.current.run { maxWidth.roundToPx() }


                viewModel.loadVisualizeSpecification(
                    bitmapWidth = width,
                    widthHeightRatio = 1080f / 1920f,
                    marginFraction = 0.05f
                )
            }
            LOADING -> {
                LoadingComposable()
            }
            STANDBY, SAVING -> {

                val permState = rememberPermissionState(permission = viewModel.permission)
                val hasPermission = remember { derivedStateOf { permState.hasPermission } }

                val lifecycleOwner = LocalLifecycleOwner.current

                // For side effects that need to be cleaned up after keys change
                // or the composable leaves the composition
                // If keys change, composable disposes current effect and resets by calling again
                DisposableEffect(
                    key1 = lifecycleOwner,
                    effect = {
                        val observer = LifecycleEventObserver { _, event ->
                            if (event == Lifecycle.Event.ON_START) {
                                permState.launchPermissionRequest()
                            }
                        }
                        lifecycleOwner.lifecycle.addObserver(observer)

                        onDispose {
                            lifecycleOwner.lifecycle.removeObserver(observer)
                        }
                    }
                )

                Column(
                    modifier = Modifier.widthIn(360.dp, maxWidth * 0.8f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Card(
                        elevation = 4.dp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp)
                            .heightIn(0.dp, maxHeight * 0.75f)
                    ) {
                        VisualizeImage(
                            bitmap = visualizeBitmapMaker(
                                viewModel.visualizationSpec!!
                            )
                        )
                    }
                    if (hasPermission.value)
                        ButtonComposable(
                            text = if (screenState == SAVING) "Saving Image" else "Save Image",
                            modifier = Modifier.fillMaxWidth(),
                            enabled = screenState != SAVING,
                            icon = Icons.Default.Save
                        ) {
                            viewModel.startSave(context)
                        }
                    else {
                        if (permState.shouldShowRationale)
                            WarningComposable()
                        ButtonComposable(
                            text = "Grant Save Permission",
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            permState.launchPermissionRequest()
                        }
                    }
                }
            }
        }
    }
}