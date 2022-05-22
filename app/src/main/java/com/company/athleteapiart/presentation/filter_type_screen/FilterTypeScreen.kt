package com.company.athleteapiart.presentation.filter_type_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.company.athleteapiart.Screen
import com.company.athleteapiart.presentation.common.*
import com.company.athleteapiart.presentation.filter_type_screen.FilterTypeScreenState.*
import com.company.athleteapiart.presentation.ui.theme.Icicle

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
    val coroutineScope  = rememberCoroutineScope()

    when (screenState) {
        LAUNCH -> SideEffect {
            viewModel.loadActivities(
                context = context,
                athleteId = athleteId,
                yearMonths = yearMonths
            )
        }
        LOADING, STANDBY -> {
            BoxWithConstraints(
                modifier = Modifier.fillMaxSize().background(Icicle),
                contentAlignment = Alignment.Center
            ) {

                val maxHeight = this.maxHeight
                val maxWidth = this.maxWidth

                Column(
                    modifier = Modifier.widthIn(360.dp, maxWidth * 0.8f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    HeaderWithEmphasisComposable(emphasized = "activity types")

                    /*
                    Table.TableComposable(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(0.dp, maxHeight * 0.6f),
                        columns = viewModel.columns,
                        rows = viewModel.rows,
                        onSelectIndex = {
                            viewModel.updateSelectedActivities(it)
                        },
                        selectionList = viewModel.selectedTypes,
                        coroutineScope = coroutineScope
                    )

                     */


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