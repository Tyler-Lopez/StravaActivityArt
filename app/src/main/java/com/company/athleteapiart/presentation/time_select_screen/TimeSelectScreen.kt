package com.company.athleteapiart.presentation.time_select_screen

import androidx.compose.foundation.*
import androidx.compose.runtime.getValue
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.material.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.navigation
import com.google.accompanist.navigation.animation.composable
import com.company.athleteapiart.Screen
import com.company.athleteapiart.presentation.composable.*
import com.company.athleteapiart.ui.spacing
import com.company.athleteapiart.ui.theme.*
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
                        navController.navigate("${Screen.FilterActivities}") {
                            popUpTo(Screen.TimeSelect.route) {
                                inclusive = true
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
                        //   ComposableAppNameHorizontal(
                        //       modifier = Modifier
                        //           .fillMaxWidth()
                        //    )
                        ComposableParagraph(
                            text = "Which year would you like to visualize" +
                                    " activities from?",
                            modifier = Modifier.padding(bottom = MaterialTheme.spacing.md)
                        )
                        ComposableShadowBox {
                            // Determine size of each button from width
                            val maxWidth = this.maxWidth
                            val yearsPerRow = if (maxWidth >= 600.dp) 3 else 2
                            val buttonSize = (maxWidth - spacingMd * yearsPerRow) / yearsPerRow

                            FlowRow(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = spacingMd)
                            ) {
                                var yearIncrement = 1;
                                for (year in TimeUtils.yearsAvailable().reversed()) {
                                    Button(
                                        onClick = {
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
                            }
                        }
                    }
                }
            }
        })
}