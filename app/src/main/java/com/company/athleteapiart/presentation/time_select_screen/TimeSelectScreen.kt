package com.company.athleteapiart.presentation.time_select_screen

import android.graphics.Paint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Checkbox
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.company.athleteapiart.Screen
import com.company.athleteapiart.presentation.common.Table
import com.company.athleteapiart.presentation.time_select_screen.TimeSelectScreenState.*
import com.company.athleteapiart.presentation.ui.theme.Lato
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
    val context = LocalContext.current

    val selectedList = remember { mutableStateListOf<Boolean>() }

    BoxWithConstraints(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        val boxHeight = this.maxHeight
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .width(360.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Which years would you like to visualize?",
                fontSize = 32.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = Lato,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(8.dp)
            )

            Table.TableComposable(
                modifier = Modifier.fillMaxWidth().height(boxHeight * 0.5f),
                columns = viewModel.getColumns(),
                rows = viewModel.getRows(),
                onSelectIndex = { selectedList[it] = !selectedList[it] }
            )


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
                    if (viewModel.activityCount > 0) {
                        for (i in 0 until viewModel.activityCount)
                            selectedList.add(false)

                        // Button will only be shown if we can continue or try again
                        Button(
                            onClick = {
                                navController.navigate(
                                    Screen.FilterMonth.withArgs(
                                        athleteId.toString(),
                                        viewModel.selectedYearsNavArgs(list = selectedList)
                                    )
                                )
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "Continue",
                                    fontFamily = Lato,
                                    fontSize = 28.sp,
                                )
                            }
                        }
                        Text(
                            text = "${viewModel.selectedActivitiesCount(list = selectedList)} selected activities",
                            fontFamily = Lato,
                            color = Color.Gray,
                            modifier = Modifier.padding(8.dp)
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