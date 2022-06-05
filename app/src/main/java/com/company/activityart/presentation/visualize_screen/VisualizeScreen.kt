package com.company.activityart.presentation.visualize_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AspectRatio
import androidx.compose.material.icons.filled.Save
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavHostController
import com.company.activityart.R
import com.company.activityart.presentation.common.*
import com.company.activityart.presentation.ui.theme.*
import com.company.activityart.presentation.visualize_screen.VisualizeScreenState.*
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
    val selectedResolution by remember { viewModel.selectedResolution }

    val bitmap = remember { viewModel.bitmapState }


    Column(modifier = Modifier.fillMaxSize()) {
        DropdownComposable(
            menuItems = viewModel.resolutions,
            message = "Size",
            onItemSelected = {
                viewModel.updateResolution(it)
            },
            modifier = Modifier.fillMaxWidth(),
            selectedIndex = viewModel.selectedResolution.value
        )

        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .background(Icicle),
            contentAlignment = Alignment.Center
        ) {
                val maxWidth = this.maxWidth
                val maxHeight = this.maxHeight

                Column(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
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
                        GET_SPECIFICATION -> {
                            val width = LocalDensity.current.run { maxWidth.roundToPx() }
                            viewModel.loadVisualizeSpecification(composableWidth = width)
                        }
                        LOADING -> LoadingComposable()
                        SAVING -> LoadingComposable("Saving to device...")
                        STANDBY -> {

                            val permState =
                                rememberPermissionState(permission = viewModel.permission)
                            val hasPermission =
                                remember { derivedStateOf { permState.hasPermission } }

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

                            HeaderWithEmphasisComposable(emphasized = "Preview", string = "Preview")

                            Card(
                                elevation = 4.dp,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .heightIn(0.dp, maxHeight * 0.75f)
                            ) {
                                bitmap.value?.let { VisualizeImage(bitmap = it) }
                            }

                            if (hasPermission.value)
                                ButtonComposable(
                                    text = "Save Image",
                                    modifier = Modifier.fillMaxWidth().height(64.dp),
                                    icon = Icons.Default.Save
                                ) {
                                    viewModel.startSave(context)
                                }
                            else {
                                if (permState.shouldShowRationale)
                                    WarningComposable()
                                ButtonComposable(
                                    text = "Grant Save Permission",
                                    modifier = Modifier.fillMaxWidth().height(64.dp)
                                ) {
                                    permState.launchPermissionRequest()
                                }

                            }
                        }
                    }
                }
        }
    }
}