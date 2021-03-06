package com.company.activityart.presentation.filter_gear_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.company.activityart.presentation.common.*
import com.company.activityart.presentation.ui.theme.Icicle
import com.company.activityart.util.ScreenState.*

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
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(Icicle),
        contentAlignment = Alignment.Center
    ) {

        val maxHeight = this.maxHeight
        val maxWidth = this.maxWidth

        ContainerColumn(maxWidth) {
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

                    HeaderWithEmphasisComposable(emphasized = "gears")

                    // Anytime rows is mutated, invoke call to convert them from ID to name
                    // TODO, this is not great
                    // improve, reduce amount of recomposition conversion
                    var convertedRows = viewModel.convertRows()
                    LaunchedEffect(viewModel.rows) { convertedRows = viewModel.convertRows() }

                    Table.TableComposable(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(0.dp, maxHeight * 0.6f),
                        columns = viewModel.columns.toList(),
                        rows = viewModel.convertRows(),
                        onSelectIndex = {
                            viewModel.updateSelectedActivities(it)
                        },
                        selectionList = viewModel.selected,
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