package com.company.athleteapiart.presentation.filter_gear_screen

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
import com.company.athleteapiart.presentation.common.Table
import com.company.athleteapiart.util.ScreenState.*
import com.company.athleteapiart.presentation.ui.theme.Lato

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
                    accessToken = accessToken,
                    yearMonths = yearMonths,
                    activityTypes = activityTypes
                )
            }
            LOADING, STANDBY -> {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.2f)
                        .padding(8.dp),
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Which gears would you like to include?",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = Lato,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(8.dp)
                    )
                }

                // Anytime rows is mutated, invoke call to convert them from ID to name
                var convertedRows = viewModel.convertRows()
                LaunchedEffect(viewModel.rows) { convertedRows = viewModel.convertRows() }

                Table.TableComposable(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.6f),
                    columns = viewModel.columns,
                    rows = convertedRows,
                    onSelectIndex = {
                        viewModel.updateSelectedActivities(it)
                    },
                    selectionList = viewModel.selected
                )


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
                            navController.navigate(
                                route = viewModel.getNavScreen().withArgs(
                                    athleteId.toString(), viewModel.yearMonthsToNavArg(yearMonths),
                                    optionalArgs = arrayOf(
                                        "types" to viewModel.selectedTypesToNavArg(activityTypes),
                                        "gears" to viewModel.selectedGearsToNavArg()
                                    )
                                )
                            )
                        }, modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text("Continue")
                    }
                    Text(
                        text = "$selectedCount SELECTED ACTIVITIES",
                        fontFamily = Lato,
                        color = Color.Gray,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        }
    }
}