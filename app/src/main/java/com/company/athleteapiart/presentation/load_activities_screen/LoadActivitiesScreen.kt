package com.company.athleteapiart.presentation.load_activities_screen

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.company.athleteapiart.presentation.destinations.ActivitiesScreenDestination
import com.company.athleteapiart.util.AthleteActivities
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun LoadActivitiesScreen(
    year: Int,
    navigator: DestinationsNavigator,
    viewModel: LoadActivitiesViewModel = hiltViewModel()
) {
    val activities = viewModel.activities
    val endReached by remember { viewModel.endReached }
    val loadError by remember { viewModel.loadError }
    val isLoading by remember { viewModel.isLoading }

    if (viewModel.activities.isEmpty() && !viewModel.isLoading.value && !endReached)
        viewModel.loadActivitiesByYear(year)

    Column() {
        if (isLoading) {
            Text("Loading Activities")
            Text("${activities.size} activities Loaded.")
        } else if (loadError != "") {
            Text("$loadError error")
            Text("${activities.size} activities Loaded.")
        } else {
            Text("${activities.size} activities Loaded.")
            Button(onClick = {
                AthleteActivities.selectedActivities = viewModel.activities
                navigator.navigate(
                    direction = ActivitiesScreenDestination
                )
            }) {
                Text("Draw")
            }
        }
    }
}