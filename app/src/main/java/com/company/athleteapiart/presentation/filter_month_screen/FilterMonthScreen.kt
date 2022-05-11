package com.company.athleteapiart.presentation.filter_month_screen

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.company.athleteapiart.presentation.filter_month_screen.FilterMonthScreenState.*

@Composable
fun FilterMonthScreen(
    athleteId: Long,
    years: Array<Int>,
    navController: NavHostController,
    viewModel: FilterMonthViewModel = hiltViewModel()
) {

    val screenState by remember { viewModel.filterMonthScreenState }
    val context = LocalContext.current

    when (screenState) {
        LAUNCH -> SideEffect {
            viewModel.loadActivities(
                context = context,
                athleteId = athleteId,
                years = years
            )
        }
        LOADING -> {
            Text("Loading")
        }
        STANDBY -> {
            val yearMonthsData = viewModel.yearMonthsData
            Column {
                for (year in years)
                    for (month in 0 until 12) {
                        yearMonthsData[Pair(year, month)]?.also {
                            Text("$year $month ${it.first}")
                        }
                    }
            }
        }
    }

}