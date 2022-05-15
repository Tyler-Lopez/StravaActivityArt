package com.company.athleteapiart.presentation.filter_distance_screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import com.company.athleteapiart.presentation.common.Table
import com.company.athleteapiart.presentation.filter_type_screen.FilterTypeViewModel
import com.company.athleteapiart.presentation.ui.theme.Lato
import com.company.athleteapiart.util.ScreenState

@Composable
fun FilterDistanceScreen(
    athleteId: Long,
    yearMonths: Array<Pair<Int, Int>>,
    navController: NavHostController,
    activityTypes: Array<String>? = null, // If null then do not filter by activityTypes
    gears: Array<String>? = null,
    viewModel: FilterDistanceViewModel = hiltViewModel()
) {
    val screenState by remember { viewModel.screenState }
    val context = LocalContext.current
    val distanceMinimum by remember { viewModel.distanceMinimum }
    val distanceMaximum by remember { viewModel.distanceMaximum }


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
            ScreenState.LOADING, ScreenState.STANDBY -> {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.2f)
                        .padding(8.dp),
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Which gears would you like to include?",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = Lato,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(8.dp)
                    )
                }

                Text("Here $distanceMinimum min $distanceMaximum max")


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
                        text = "SELECTED ACTIVITIES",
                        fontFamily = Lato,
                        color = Color.Gray,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        }
    }
}