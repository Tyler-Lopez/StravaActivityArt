package com.company.athleteapiart.presentation.time_select_screen

import androidx.compose.runtime.getValue
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.material.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.company.athleteapiart.presentation.activity_select_screen.composable.ComposableReturnButton
import com.company.athleteapiart.presentation.activity_select_screen.composable.ComposableTopBar
import com.company.athleteapiart.presentation.destinations.ActivitiesScreenDestination
import com.company.athleteapiart.presentation.destinations.LoadActivitiesScreenDestination
import com.company.athleteapiart.presentation.load_activities_screen.LoadActivitiesScreen
import com.company.athleteapiart.ui.theme.*
import com.company.athleteapiart.util.AthleteActivities
import com.company.athleteapiart.util.TimeUtils
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator


@OptIn(ExperimentalPermissionsApi::class)
@Destination(start = true)
@Composable
fun TimeSelectScreen(
    navigator: DestinationsNavigator,
    viewModel: TimeSelectViewModel = hiltViewModel()
) {
    val endReached by remember { viewModel.endReached }
    val loadError by remember { viewModel.loadError }
    val isLoading by remember { viewModel.isLoading }

    Scaffold(
        topBar = {
            ComposableTopBar {
                ComposableReturnButton(onClick = { navigator.navigateUp() })
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
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
                for (year in TimeUtils.yearsAvailable()) {
                    Button(onClick = {
                        navigator.navigate(
                            direction = LoadActivitiesScreenDestination(year)
                        )
                    }) {
                        Text("$year")
                    }
                }
            }
        }
    )
}