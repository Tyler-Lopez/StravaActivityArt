package com.company.athleteapiart.presentation.filter_distance_screen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.RangeSlider
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.company.athleteapiart.Screen
import com.company.athleteapiart.presentation.common.ActivitiesCountComposable
import com.company.athleteapiart.presentation.common.ButtonWithCountComposable
import com.company.athleteapiart.presentation.common.HeaderWithEmphasisComposable
import com.company.athleteapiart.presentation.ui.theme.Lato
import com.company.athleteapiart.presentation.ui.theme.StravaOrange
import com.company.athleteapiart.util.ScreenState
import kotlinx.coroutines.launch

@ExperimentalMaterialApi
@Composable
fun FilterDistanceScreen(
    athleteId: Long,
    yearMonths: Array<Pair<Int, Int>>,
    navController: NavHostController,
    activityTypes: Array<String>? = null, // If null then do not filter by activityTypes
    gears: Array<String?>? = null, // If null do not filter, if string is null then that means null gearId is included
    viewModel: FilterDistanceViewModel = hiltViewModel()
) {

    val screenState by remember { viewModel.screenState }
    val context = LocalContext.current
    val range by remember { viewModel.distanceRange }
    val selected by remember { viewModel.selectedRange }
    val selectedCount by remember { viewModel.selectedCount }

    val distanceRangeInt by remember { viewModel.distanceRangeInt }
    val distancesHeightMap by remember { viewModel.distancesHeightMap }
    val selectedMiles by remember { viewModel.selectedMiles }

    when (screenState) {
        ScreenState.LAUNCH -> SideEffect {
            viewModel.loadActivities(
                context = context,
                athleteId = athleteId,
                yearMonths = yearMonths,
                activityTypes = activityTypes,
                gears = gears
            )
        }
        ScreenState.LOADING -> {

        }
        ScreenState.STANDBY -> {
            BoxWithConstraints(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                LaunchedEffect(Unit) {
                }
                val maxHeight = this.maxHeight
                val maxWidth = this.maxWidth

                Column(
                    modifier = Modifier.widthIn(360.dp, maxWidth * 0.8f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    HeaderWithEmphasisComposable(emphasized = "distances")
                    DistanceGraphComposable(
                        distanceRange = distanceRangeInt ?: 0..0,
                        distancesHeightMap = distancesHeightMap ?: mapOf(),
                        maxHeight = maxHeight,
                        selectedMiles = selectedMiles ?: 0..0
                    )
                    RangeSlider(
                        values = selected,
                        valueRange = range,
                        onValueChange = {
                            viewModel.onSelectedChange(it)
                        })
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "%.1f".format(selected.start),
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = Lato
                        )
                        Text(
                            text = "to",
                            fontSize = 24.sp,
                            fontFamily = Lato
                        )
                        Text(
                            text = "%.1f".format(selected.endInclusive),
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = Lato
                        )
                        Text(
                            text = "miles",
                            fontSize = 24.sp,
                            fontFamily = Lato
                        )
                    }
                    ActivitiesCountComposable(count = selectedCount)
                    ButtonWithCountComposable(activitiesEmpty = selectedCount == 0) {
                        navController.navigate(
                            route = Screen.VisualizeActivities.withArgs(
                                athleteId.toString(),
                                viewModel.yearMonthsNavArgs(yearMonths),
                                optionalArgs = arrayOf(
                                    "types" to viewModel.activityTypesNavArgs(activityTypes),
                                    "gears" to viewModel.gearsNavArgs(gears),
                                    "distances" to viewModel.distancesNavArgs()
                                )
                            )
                        )
                    }
                }
            }
        }
    }
}
