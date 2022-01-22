package com.company.athleteapiart.presentation.filter_activities_screen

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.company.athleteapiart.presentation.composable.*
import com.company.athleteapiart.presentation.destinations.ActivitiesScreenDestination
import com.company.athleteapiart.presentation.destinations.TimeSelectScreenDestination
import com.company.athleteapiart.ui.theme.*
import com.company.athleteapiart.util.AthleteActivities
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun FilterActivitiesScreen(
    navigator: DestinationsNavigator,
    viewModel: FilterActivitiesViewModel = hiltViewModel()
) {
    val isLoading by remember { viewModel.isLoading }

    Scaffold(
        topBar = {
            ComposableTopBar {
                ComposableReturnButton(onClick = { navigator.navigate(TimeSelectScreenDestination) })
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        text = "",
                        fontFamily = Roboto,
                        fontSize = 20.sp,
                        color = White,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
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
                        ComposableParagraph(
                            text = "${viewModel.activitySize} Activities Loaded",
                            color = StravaOrange,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp, start = 20.dp, end = 20.dp)
                        )
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(0.9f)
                                .padding(vertical = 10.dp, horizontal = 20.dp)
                                .background(WarmGrey40)
                                .border(
                                    width = 2.dp,
                                    color = WarmGrey20
                                )

                        ) {

                        }
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(0.1f),
                            contentAlignment = Alignment.Center
                        ) {
                            Button(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(bottom = 10.dp, start = 20.dp, end = 20.dp),
                                onClick = {
                                    AthleteActivities.selectedActivities = AthleteActivities.activities.value
                                    navigator.navigate(ActivitiesScreenDestination)
                                }
                            ) {
                                ComposableHeader(
                                    text = "Apply Filters",
                                    color = White
                                )
                            }
                        }
                    }
                }
            }
        }
    )
}