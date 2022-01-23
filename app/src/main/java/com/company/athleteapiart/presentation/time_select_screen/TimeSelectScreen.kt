package com.company.athleteapiart.presentation.time_select_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.runtime.getValue
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.material.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.company.athleteapiart.presentation.composable.ComposableAppNameHorizontal
import com.company.athleteapiart.presentation.composable.ComposableParagraph
import com.company.athleteapiart.presentation.composable.ComposableTopBar
import com.company.athleteapiart.presentation.destinations.LoadActivitiesScreenDestination
import com.company.athleteapiart.ui.theme.*
import com.company.athleteapiart.util.TimeUtils
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator


@OptIn(ExperimentalPermissionsApi::class)
@Destination(start = true)
@Composable
fun TimeSelectScreen(
    navigator: DestinationsNavigator,
    viewModel: TimeSelectViewModel = hiltViewModel()
) {
    val endReached by remember { viewModel.endReached }
    val loadError by remember { viewModel.loadError }
    val isLoading by remember { viewModel.isLoading }

    Scaffold(
        topBar = {
            ComposableTopBar(null, null)
        },
        content = {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ComposableAppNameHorizontal(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp)
                )
                ComposableParagraph(
                    text = "To create a print of your activities, " +
                            "begin by selecting which span of time you " +
                            "would like to visualize activities from.",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp, horizontal = 20.dp)
                )
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(vertical = 10.dp, horizontal = 20.dp)
                        .background(WarmGrey40)
                        .border(
                            width = 2.dp,
                            color = WarmGrey20
                        )

                ) {
                    item { Spacer(modifier = Modifier.height(5.dp)) }
                    for (year in TimeUtils.yearsAvailable().reversed()) {
                        item {
                            Button(
                                onClick = {
                                    navigator.navigate(
                                        direction = LoadActivitiesScreenDestination(year)
                                    )
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 5.dp, horizontal = 20.dp),
                                colors = ButtonDefaults.buttonColors(backgroundColor = WarmGrey20)
                            ) {
                                Text(
                                    text = "$year",
                                    fontFamily = Roboto,
                                    fontSize = 30.sp,
                                    fontWeight = FontWeight.Light,
                                    color = StravaOrange
                                )
                            }
                        }
                    }
                    item { Spacer(modifier = Modifier.height(5.dp)) }
                }
            }
        }
    )
}