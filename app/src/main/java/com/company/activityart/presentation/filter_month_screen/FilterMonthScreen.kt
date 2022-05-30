package com.company.activityart.presentation.filter_month_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.company.activityart.Screen
import com.company.activityart.presentation.common.*
import com.company.activityart.presentation.filter_month_screen.FilterMonthScreenState.*
import com.company.activityart.presentation.ui.theme.Icicle

@Composable
fun FilterMonthScreen(
    athleteId: Long,
    accessToken: String,
    years: Array<Int>,
    navController: NavHostController,
    viewModel: FilterMonthViewModel = hiltViewModel()
) {

    val screenState by remember { viewModel.filterMonthScreenState }
    val context = LocalContext.current
    val selectedActivitiesCount by remember { viewModel.selectedCount }

    println("recomposed")

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(Icicle),
        contentAlignment = Alignment.Center
    ) {
        val maxHeight = this.maxHeight
        ContainerColumn(maxWidth) {
            when (screenState) {
                LAUNCH -> {
                    SideEffect {
                        viewModel.loadActivities(
                            context = context,
                            athleteId = athleteId,
                            years = years
                        )
                    }
                }
                LOADING -> {
                    LoadingComposable()
                }
                STANDBY -> {
                    HeaderWithEmphasisComposable(emphasized = "months")
                    Table.TableComposable(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(0.dp, maxHeight * 0.6f),
                        columns = viewModel.columns.toList(),
                        rows = viewModel.rows,
                        onSelectIndex = {
                            viewModel.updateSelectedActivities(it)
                        },
                        selectionList = viewModel.selected
                    )

                    if (screenState == LOADING)
                        LoadingComposable()

                    ActivitiesCountComposable(count = selectedActivitiesCount)
                    ButtonWithCountComposable(activitiesEmpty = selectedActivitiesCount == 0) {
                        navController.navigate(
                            Screen.FilterType.withArgs(
                                athleteId.toString(),
                                accessToken,
                                viewModel.yearMonthsNavArgs()
                            )
                        )
                    }
                }
            }
        }
    }
}