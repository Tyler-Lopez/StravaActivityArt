package com.company.athleteapiart.presentation.filter_gear_screen

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.company.athleteapiart.presentation.common.*
import com.company.athleteapiart.util.ScreenState.*

@Composable
fun FilterGearScreen(
    athleteId: Long,
    accessToken: String,
    yearMonths: Array<Pair<Int, Int>>,
    navController: NavHostController,
    activityTypes: Array<String>? = null, // If null then do not filter by activityTypes
    viewModel: FilterGearViewModel = hiltViewModel()
) {

    val screenState by remember { viewModel.screenState }
    val context = LocalContext.current
    val selectedCount by remember { viewModel.selectedCount }


    when (screenState) {
        LAUNCH -> SideEffect {
            viewModel.loadActivities(
                context = context,
                athleteId = athleteId,
                accessToken = accessToken,
                yearMonths = yearMonths,
                activityTypes = activityTypes
            )
        }
        LOADING, STANDBY -> {
            BoxWithConstraints(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {

                val maxHeight = this.maxHeight
                val maxWidth = this.maxWidth

                Column(
                    modifier = Modifier.widthIn(360.dp, maxWidth * 0.8f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    HeaderWithEmphasisComposable(emphasized = "gears")

                    // Anytime rows is mutated, invoke call to convert them from ID to name
                    var convertedRows = viewModel.convertRows()
                    LaunchedEffect(viewModel.rows) { convertedRows = viewModel.convertRows() }
                    Table.TableComposable(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(0.dp, maxHeight * 0.6f),
                        columns = viewModel.columns,
                        rows = convertedRows,
                        onSelectIndex = {
                            viewModel.updateSelectedActivities(it)
                        },
                        selectionList = viewModel.selected
                    )

                    if (screenState == LOADING)
                        LoadingComposable()

                    ActivitiesCountComposable(count = selectedCount)
                    ButtonWithCountComposable(activitiesEmpty = selectedCount == 0) {
                        navController.navigate(
                            route = viewModel.getNavScreen().withArgs(
                                athleteId.toString(),
                                viewModel.yearMonthsNavArgs(yearMonths),
                                optionalArgs = arrayOf(
                                    "types" to viewModel.activityTypesNavArgs(activityTypes),
                                    "gears" to viewModel.gearsNavArgs()
                                )
                            )
                        )
                    }
                }
            }
        }
    }
}