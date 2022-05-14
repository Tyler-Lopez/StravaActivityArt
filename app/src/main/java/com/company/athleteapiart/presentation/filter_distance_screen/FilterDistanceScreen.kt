package com.company.athleteapiart.presentation.filter_distance_screen

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.company.athleteapiart.presentation.filter_type_screen.FilterTypeViewModel

@Composable
fun FilterDistanceScreen(
    athleteId: Long,
    yearMonths: Array<Pair<Int, Int>>,
    navController: NavHostController,
    activityTypes: Array<String>? = null, // If null then do not filter by activityTypes
    gears: Array<String>? = null,
    viewModel: FilterDistanceViewModel = hiltViewModel()
) {
    Text("Here at distance screen")
}