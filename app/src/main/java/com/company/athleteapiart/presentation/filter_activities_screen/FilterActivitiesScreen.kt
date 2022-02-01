package com.company.athleteapiart.presentation.filter_activities_screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.company.athleteapiart.Screen
import com.company.athleteapiart.presentation.composable.*
import com.company.athleteapiart.ui.theme.*
import com.company.athleteapiart.util.AthleteActivities
import com.company.athleteapiart.util.monthFromIso8601
import com.company.athleteapiart.util.navigateAndReplaceStartRoute

@Composable
fun FilterActivitiesScreen(
    navController: NavHostController,
    viewModel: FilterActivitiesViewModel = hiltViewModel()
) {
    val isLoading by remember { viewModel.isLoading }
    val minDistanceSlider by remember { viewModel.minDistanceSlider }
    val maxDistanceSlider by remember { viewModel.maxDistanceSlider }

    // We cleared the stack prior to opening this screen
    // Ensure if you push back it goes back to selecting a year and does not close the app
    BackHandler {
        navController.backQueue.clear()
        navController.navigate(Screen.TimeSelect.route)
    }

    Scaffold(
        topBar = {
            ComposableTopBar(
                leftContent = {
                    ComposableReturnButton {
                        navController.backQueue.clear()
                        navController.navigate(Screen.TimeSelect.route)
                    }
                }, null
            )
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                when {
                    isLoading -> {
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
                                text = "Processing",
                                isBold = true
                            )
                        }
                    }
                    else -> {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(0.9f)
                                .padding(vertical = 10.dp, horizontal = 10.dp)
                                .background(WarmGrey40)
                                .border(
                                    width = 2.dp,
                                    color = WarmGrey20
                                )

                        ) {
                            item {
                                ComposableItemContainer {
                                    ComposableHeader(
                                        text = "Months",
                                        color = StravaOrange
                                    )
                                    for (month in viewModel.months.reversed()) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Checkbox(
                                                checked = viewModel
                                                    .checkboxes
                                                    .getOrDefault(
                                                        month,
                                                        false
                                                    ),
                                                onCheckedChange = {
                                                    viewModel.flipValue(month)
                                                })
                                            ComposableParagraph(text = month)
                                        }
                                    }
                                }
                            }

                            item {
                                ComposableItemContainer {
                                    ComposableHeader(
                                        text = "Activity Types",
                                        color = StravaOrange
                                    )
                                    for (activity in viewModel.activityTypes) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Checkbox(
                                                checked = viewModel
                                                    .checkboxes
                                                    .getOrDefault(
                                                        activity,
                                                        false
                                                    ),
                                                onCheckedChange = {
                                                    viewModel.flipValue(activity)
                                                })
                                            ComposableParagraph(text = activity)
                                        }
                                    }
                                }
                            }
                            item {
                                ComposableItemContainer {
                                    ComposableHeader(
                                        text = "Distance",
                                        color = StravaOrange
                                    )
                                    val valueRange =
                                        viewModel.minimumDistance..viewModel.maximumDistance
                                    ComposableDistanceSlider(
                                        header = "Minimum Distance",
                                        value = minDistanceSlider,
                                        valueRange = valueRange,
                                        onValueChange = { viewModel.setMinDistanceSlider(it) }
                                    )
                                    ComposableDistanceSlider(
                                        header = "Maximum Distance",
                                        value = maxDistanceSlider,
                                        valueRange = valueRange,
                                        onValueChange = { viewModel.setMaxDistanceSlider(it) }
                                    )
                                }
                            }
                            item {
                                Spacer(
                                    modifier = Modifier.height(250.dp)
                                )
                            }
                        }
                    }
                }
            }
        },
        bottomBar = {
            ComposableLargeButton(
                text = "Apply Filters",
                onClick = {

                    AthleteActivities.filteredActivities.value.clear()

                    for (activity in AthleteActivities.activities.value) {
                        // Filter undesired months, activities, and distances
                        val activityMonth = activity.start_date_local.monthFromIso8601()
                        val activityType = activity.type
                        if (
                        // Month check
                            !(viewModel.checkboxes.getOrDefault(activityMonth, false)) ||
                            // Type check
                            !(viewModel.checkboxes.getOrDefault(activityType, false)) ||
                            // Distance check
                            activity.distance < viewModel.minDistanceSlider.value ||
                            activity.distance > viewModel.maxDistanceSlider.value
                        ) {
                            continue
                        }
                        AthleteActivities.filteredActivities.value.add(activity)
                    }
                    navController.navigate("${Screen.FormatActivitiesOne}")
                }
            )
        })
}