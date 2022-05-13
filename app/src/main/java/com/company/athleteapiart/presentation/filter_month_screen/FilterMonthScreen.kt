package com.company.athleteapiart.presentation.filter_month_screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
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
import com.company.athleteapiart.presentation.filter_month_screen.FilterMonthScreenState.*
import com.company.athleteapiart.presentation.ui.theme.Lato

@Composable
fun FilterMonthScreen(
    athleteId: Long,
    years: Array<Int>,
    navController: NavHostController,
    viewModel: FilterMonthViewModel = hiltViewModel()
) {

    val screenState by remember { viewModel.filterMonthScreenState }
    val context = LocalContext.current

    val selectedActivitiesCount by remember { viewModel.selectedActivitiesCount }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(64.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
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
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.2f)
                        .padding(8.dp),
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Which months would you like to include?",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = Lato,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(8.dp)
                    )
                }
                /*
                Table.TableComposable(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.6f),
                    columns = viewModel.getColumns(),
                    rows = viewModel.getRows(),
                    onSelectIndex = {
                        viewModel.updateSelectedActivities(it)
                    },
                    defaultSelected = true,
                    savedState = viewModel.selectedActivities
                )

                 */
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.2f)
                        .padding(8.dp),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(
                        onClick = {
                            if (viewModel.mustFilterActivityType()) {
                                println("HERE ${viewModel.selectedMonthYearsNavArgs}")
                                navController.navigate(
                                    Screen.FilterType.withArgs(
                                        athleteId.toString(),
                                        viewModel.selectedMonthYearsNavArgs
                                    )
                                )
                            }
                        }, modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text("Continue")
                    }
                    Text(
                        text = "$selectedActivitiesCount SELECTED ACTIVITIES",
                        fontFamily = Lato,
                        color = Color.Gray,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        }
    }
}