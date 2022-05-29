package com.company.activityart.presentation.filter_year_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.company.activityart.Screen
import com.company.activityart.presentation.common.*
import com.company.activityart.presentation.filter_year_screen.TimeSelectScreenState.*
import com.company.activityart.presentation.ui.theme.Icicle

/*

 FilterYearScreen

 This screen takes the athleteId and accessToken to load all activities from the API into ROOM
 Upon loading all activities, the user may choose which years they would like to take into the next

 After this Screen, we should never call the API again in workflow

 */

@Composable
fun FilterYearScreen(
    athleteId: Long,
    accessToken: String,
    navController: NavHostController,
    viewModel: TimeSelectViewModel = hiltViewModel()
) {

    val screenState by remember { viewModel.timeSelectScreenState }
    val context = LocalContext.current
    val selectedActivitiesCount by remember { viewModel.selectedActivitiesCount }
    val coroutineScope  = rememberCoroutineScope()

    when (screenState) {
        LAUNCH -> SideEffect {
            viewModel.loadActivities(
                context = context,
                athleteId = athleteId,
                accessToken = accessToken
            )
        }
        LOADING, STANDBY, ERROR -> {
            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Icicle),
                contentAlignment = Alignment.Center
            ) {

                val maxHeight = this.maxHeight
                val maxWidth = this.maxWidth

                Column(
                    modifier = Modifier.widthIn(360.dp, maxWidth * 0.8f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    println("Here in ${viewModel.rows}")
                    HeaderWithEmphasisComposable(emphasized = "years")
                    Table.TableComposable(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(0.dp, maxHeight * 0.6f),
                        columns = viewModel.columns.toList(),
                        rows = viewModel.rows.toList(),
                        onSelectIndex = {
                            viewModel.updateSelectedActivities(it)
                        },
                        selectionList = viewModel.selectedActivities,
                    )

                    if (screenState == LOADING)
                        LoadingComposable()

                    ActivitiesCountComposable(count = selectedActivitiesCount)
                    ButtonWithCountComposable(activitiesEmpty = selectedActivitiesCount == 0) {
                        navController.navigate(
                            Screen.FilterMonth.withArgs(
                                athleteId.toString(),
                                accessToken,
                                viewModel.selectedYearsNavArgs()
                            )
                        )
                    }

                    // If there is an error, allow user to try to load activities again
                    if (screenState == ERROR) {
                        Button(onClick = {
                            viewModel.loadActivities(
                                context = context,
                                athleteId = athleteId,
                                accessToken = accessToken
                            )
                        }) {
                            Text("ERROR")
                        }
                    }
                }
            }
        }
    }
}