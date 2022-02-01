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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.company.athleteapiart.Screen
import com.company.athleteapiart.presentation.composable.ComposableAppNameHorizontal
import com.company.athleteapiart.presentation.composable.ComposableHeader
import com.company.athleteapiart.presentation.composable.ComposableParagraph
import com.company.athleteapiart.presentation.composable.ComposableTopBar
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
            ComposableTopBar(null, null)
        },
        content = {
            when {
                // If the user just selected a YEAR
                // Get activities from Strava API
                isLoading -> {
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
                        Text("${activities.size} Activities Loaded")
                    }
                }
                // If error occurred when fetching data
                loadError != "" -> {

                }
                // If data has been successfully received
                endReached -> {
                    navController.navigate("${Screen.FilterActivities}")
                }
                // Otherwise, present options to user
                else -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(spacingMd),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        ComposableAppNameHorizontal(
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                        ComposableParagraph(
                            text = "To create a print of your activities, " +
                                    "begin by selecting which span of time you " +
                                    "would like to visualize activities from.",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = spacingMd)
                        )
                        BoxWithConstraints(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = spacingMd)
                        ) {

                            // Determine size of each button from width
                            val maxWidth = this.maxWidth
                            val yearsPerRow = if (maxWidth >= 600.dp) 3 else 2
                            val buttonSize = (maxWidth - spacingMd * yearsPerRow) / yearsPerRow

                            FlowRow(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        brush = Brush
                                            .verticalGradient(
                                                colors = listOf(
                                                    WarmGrey30,
                                                    WarmGrey40
                                                )
                                            ),
                                    )
                                    .border(
                                        width = spacingXxs,
                                        color = WarmGrey50
                                    )
                                    .padding(
                                        start = spacingMd
                                    )
                                    .verticalScroll(rememberScrollState())
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

                                Spacer(
                                    modifier = Modifier
                                        .height(spacingMd)
                                        .fillMaxWidth()
                                )
                            }
                        }
                    }
                }
            }
        })
}