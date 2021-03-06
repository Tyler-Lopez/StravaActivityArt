package com.company.activityart.presentation.filter_type_screen

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
import com.company.activityart.presentation.filter_type_screen.FilterTypeScreenState.*
import com.company.activityart.presentation.ui.theme.Icicle

@Composable
fun FilterTypeScreen(
    athleteId: Long,
    accessToken: String,
    yearMonths: Array<Pair<Int, Int>>,
    navController: NavHostController,
    viewModel: FilterTypeViewModel = hiltViewModel()
) {

    val screenState by remember { viewModel.filterTypeScreenState }
    val context = LocalContext.current
    val selectedTypesCount by remember { viewModel.selectedTypesCount }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(Icicle),
        contentAlignment = Alignment.Center
    ) {

        val maxHeight = this.maxHeight

        ContainerColumn(maxWidth) {

            when (screenState) {
                LAUNCH -> SideEffect {
                    viewModel.loadActivities(
                        context = context,
                        athleteId = athleteId,
                        yearMonths = yearMonths
                    )
                }
                LOADING, STANDBY -> {
                    HeaderWithEmphasisComposable(emphasized = "activity types")

                    Table.TableComposable(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(0.dp, maxHeight * 0.6f),
                        columns = viewModel.columns.toList(),
                        rows = viewModel.rows,
                        onSelectIndex = {
                            viewModel.updateSelectedActivities(it)
                        },
                        selectionList = viewModel.selectedTypes,
                    )

                    if (screenState == LOADING)
                        LoadingComposable()

                    ActivitiesCountComposable(count = selectedTypesCount)
                    ButtonWithCountComposable(activitiesEmpty = selectedTypesCount == 0) {
                        val nextScreen = viewModel.getNavScreen()
                        navController.navigate(
                            route = nextScreen.withArgs(
                                athleteId.toString(),
                                if (nextScreen is Screen.FilterGear) accessToken else "",
                                viewModel.yearMonthsNavArgs(yearMonths),
                                optionalArgs = arrayOf(
                                    "types" to viewModel.activityTypesNavArgs()
                                )
                            )
                        )
                    }
                }
            }
        }
    }
}