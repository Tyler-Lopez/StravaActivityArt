package com.company.athleteapiart.presentation.time_select_screen

import android.graphics.Paint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.Checkbox
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.company.athleteapiart.Screen
import com.company.athleteapiart.presentation.time_select_screen.TimeSelectScreenState.*
import com.company.athleteapiart.util.Constants

/*

 TimeSelectScreen

 This screen takes the athleteId and accessToken to load all activities from the API into ROOM
 Upon loading all activities, the user may choose which years they would like to take into the next

 After this Screen, we should never call the API again in workflow

 */

@Composable
fun TimeSelectScreen(
    athleteId: Long,
    accessToken: String,
    navController: NavHostController,
    viewModel: TimeSelectViewModel = hiltViewModel()
) {
    val screenState by remember { viewModel.timeSelectScreenState }
    val activities = viewModel.loadedActivities
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Select which years you would like to visualize from - you will be able to filter further at a later step",
            textAlign = TextAlign.Center
        )

        LazyColumn {
            items(viewModel.loadedActivities.size) { i ->

                val year = activities[i]
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Checkbox(checked = year.third, onCheckedChange = {
                        activities[i] = Triple(
                            year.first, year.second, !year.third
                        )
                    })
                    // YEAR
                    Text(
                        text = "${year.first}",
                        fontSize = 28.sp
                    )
                    // ACTIVITY COUNT IN YEAR
                    Text(text = "${year.second}")
                }
            }
        }

        when (screenState) {
            LAUNCH -> SideEffect {
                viewModel.loadActivities(
                    context = context,
                    athleteId = athleteId,
                    accessToken = accessToken
                )
            }
            LOADING, STANDBY, ERROR -> {
                // Either displays LOADING, or "" for FINISHED LOADING or an ERROR
                Text(viewModel.message)
                // Show selected activities if not empty
                if (activities.isNotEmpty()) {
                    Text("${viewModel.selectedActivitiesCount} Total Activities Selected")
                    // Button will only be shown if we can continue or try again
                    Button(onClick = {
                        navController.navigate(
                            Screen.FilterActivities.withArgs(
                                athleteId.toString(),
                                accessToken,
                                viewModel.selectedYearsNavArg
                            )
                        )
                    }) {
                        Text("Continue")
                    }
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