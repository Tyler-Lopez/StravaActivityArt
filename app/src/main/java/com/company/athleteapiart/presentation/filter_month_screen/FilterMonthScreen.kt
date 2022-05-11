package com.company.athleteapiart.presentation.filter_month_screen

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.company.athleteapiart.presentation.time_select_screen.TimeSelectViewModel

@Composable
fun FilterMonthScreen(
    athleteId: Long,
    years: Array<Int>,
    navController: NavHostController,
    viewModel: FilterMonthViewModel = hiltViewModel()
) {

}