package com.company.athleteapiart.presentation.filter_gear_screen

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.company.athleteapiart.presentation.filter_type_screen.FilterTypeViewModel

@Composable
fun FilterGearScreen(
    athleteId: Long,
    yearMonths: Array<Pair<Int, Int>>,
    navController: NavHostController,
    activityTypes: Array<String>? = null, // If null then do not filter by activityTypes
    viewModel: FilterGearViewModel = hiltViewModel()
) {
}