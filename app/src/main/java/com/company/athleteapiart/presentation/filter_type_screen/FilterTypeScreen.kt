package com.company.athleteapiart.presentation.filter_type_screen

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.company.athleteapiart.presentation.filter_month_screen.FilterMonthViewModel

@Composable
fun FilterTypeScreen(
    athleteId: Long,
    yearMonths: Array<Pair<Int, Int>>,
    navController: NavHostController,
    viewModel: FilterMonthViewModel = hiltViewModel()
) {
    Text(text = yearMonths.joinToString { "${it.first}\t${it.second}\t" })
}