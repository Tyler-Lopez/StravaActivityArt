package com.company.athleteapiart.presentation.filter_distance_screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.RangeSlider
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.company.athleteapiart.presentation.ui.theme.Lato
import com.company.athleteapiart.util.ScreenState

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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(64.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
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
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.2f)
                        .padding(8.dp),
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Which distances would you like to include?",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = Lato,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(8.dp)
                    )
                }

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
                RangeSlider(
                    values = selected,
                    valueRange = range,
                    onValueChange = {
                        viewModel.onSelectedChange(it)
                    })



                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.2f)
                        .padding(8.dp),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(
                        onClick = {

                        }, modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text("Continue")
                    }
                    Text(
                        text = "$selectedCount SELECTED ACTIVITIES",
                        fontFamily = Lato,
                        color = Color.Gray,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        }
    }
}