package com.company.athleteapiart.presentation.load_activities_screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.company.athleteapiart.presentation.composable.ComposableHeader
import com.company.athleteapiart.presentation.composable.ComposableReturnButton
import com.company.athleteapiart.presentation.composable.ComposableTopBar
import com.company.athleteapiart.presentation.destinations.FilterActivitiesScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

// MAY BE DELETED SOON, IMPLEMENTED IN TIME SELECT INSTEAD
@Composable
fun LoadActivitiesScreen(
    year: Int,
    navController: NavHostController,
    viewModel: LoadActivitiesViewModel = hiltViewModel()
) {
    val activities = viewModel.activities
    val endReached by remember { viewModel.endReached }
    val loadError by remember { viewModel.loadError }
    val isLoading by remember { viewModel.isLoading }

    if (viewModel.activities.isEmpty() && !viewModel.isLoading.value && !endReached)
        viewModel.loadActivitiesByYear(year)

    Scaffold(
        topBar = {
            ComposableTopBar(null, null)
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
                                text = "Loading",
                                isBold = true
                            )
                        }
                        Text("${activities.size} Activities Loaded")
                    }
                    loadError != "" -> {
                        Text("$loadError error")
                        Text("${activities.size} activities Loaded.")
                    }
                    else -> {
                   //     navigator.navigate(direction = FilterActivitiesScreenDestination())
                    }
                }
            }
        }
    )
}