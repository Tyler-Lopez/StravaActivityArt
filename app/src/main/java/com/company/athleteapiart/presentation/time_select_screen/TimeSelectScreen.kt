package com.company.athleteapiart.presentation.time_select_screen

import androidx.compose.runtime.getValue
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.material.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.company.athleteapiart.Screen
import com.company.athleteapiart.presentation.composable.*
import com.company.athleteapiart.presentation.ui.spacing
import com.company.athleteapiart.presentation.ui.theme.*
import com.company.athleteapiart.util.AthleteActivities
import com.company.athleteapiart.util.TimeUtils
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.permissions.ExperimentalPermissionsApi


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun TimeSelectScreen(
    navController: NavHostController,
    viewModel: TimeSelectViewModel = hiltViewModel()
) {


    val activities = viewModel.activities
    val endReached by remember { viewModel.endReached }
    val loadError by remember { viewModel.loadError }
    val isLoading by remember { viewModel.isLoading }

    val spacingMd = MaterialTheme.spacing.md
    val spacingXxs = MaterialTheme.spacing.xxs

    Scaffold(
        topBar = {
            ComposableTopBar(null,
                rightContent = {
                    ComposableSubtext(
                        text = "Home",
                        color = Color.White
                    )
                })
        },
        content = {
            when {
                // If the user just selected a YEAR
                // Get activities from Strava API
                isLoading || endReached -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        CircularProgressIndicator(
                            color = MaterialTheme.colors.primary
                        )
                        Row(
                            modifier = Modifier
                                .padding(vertical = 10.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.Bottom,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            ComposableHeader(
                                text = "Loading",
                                isBold = true
                            )
                        }
                    }
                    // If data has been successfully received
                    if (endReached) {
                        if (viewModel.activities.size <= 0) {
                            navController.navigate("${Screen.ErrorNoActivities}") {
                                popUpTo(Screen.TimeSelect.route) {
                                    inclusive = true
                                }
                            }
                        }
                        else {
                            navController.navigate("${Screen.FilterActivities}") {
                                popUpTo(Screen.TimeSelect.route) {
                                    inclusive = true
                                }
                            }
                        }
                    }
                }
                // If error occurred when fetching data
                loadError != "" -> {

                }
                // Otherwise, present options to user
                else -> {
                    ComposableScreenWrapper {
                        ComposableParagraph(
                            text = "Which year would you like to visualize" +
                                    " activities from?",
                            modifier = Modifier.padding(MaterialTheme.spacing.md)
                        )
                        ComposableShadowBox {
                            // Determine size of each button from width
                            val maxWidth = this.maxWidth
                            val yearsPerRow = if (maxWidth >= 600.dp) 3 else if (maxWidth >= 414.dp) 2 else 1
                            println(maxWidth.toString() + " MAX WIDTH")
                            val buttonSize = (maxWidth - spacingMd * yearsPerRow) / yearsPerRow

                            FlowRow(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = spacingMd, end = if (yearsPerRow <= 1) spacingMd else 0.dp)
                            ) {
                                var yearIncrement = 1;
                                for (year in TimeUtils.yearsAvailable().reversed()) {
                                    Button(
                                        onClick = {
                                            AthleteActivities.formatting.value.rightString = "$year"
                                            viewModel.loadActivitiesByYear(year)
                                        },
                                        modifier = Modifier
                                            .width(buttonSize)
                                            .padding(
                                                end = if (yearIncrement % yearsPerRow == 0) 0.dp else spacingMd,
                                                top = spacingMd
                                            ),
                                        colors = ButtonDefaults.buttonColors(backgroundColor = StravaOrange)
                                    ) {
                                        Text(
                                            text = "$year",
                                            fontFamily = Lato,
                                            fontSize = 30.sp,
                                            letterSpacing = 4.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = White
                                        )
                                    }
                                    yearIncrement++
                                }
                                Spacer(modifier=Modifier.height(spacingMd).fillMaxWidth())
                            }
                        }
                    }
                }
            }
        })
}