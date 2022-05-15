package com.company.athleteapiart.presentation.filter_type_screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import com.company.athleteapiart.presentation.filter_type_screen.FilterTypeScreenState.*
import com.company.athleteapiart.presentation.ui.theme.Lato

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
                    yearMonths = yearMonths
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
                        text = "Which activity types would you like to include?",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = Lato,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(8.dp)
                    )
                }

                Table.TableComposable(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.6f),
                    columns = viewModel.columns,
                    rows = viewModel.rows,
                    onSelectIndex = {
                        viewModel.updateSelectedActivities(it)
                    },
                    selectionList = viewModel.selectedTypes
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
                                    athleteId.toString(), accessToken, viewModel.yearMonthsToNavArg(yearMonths),
                                    optionalArgs = arrayOf(
                                        "types" to viewModel.selectedTypesToNavArg()
                                    )
                                )
                            )
                        }, modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text("Continue")
                    }
                    Text(
                        text = "$selectedTypesCount SELECTED ACTIVITIES",
                        fontFamily = Lato,
                        color = Color.Gray,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        }
    }
}