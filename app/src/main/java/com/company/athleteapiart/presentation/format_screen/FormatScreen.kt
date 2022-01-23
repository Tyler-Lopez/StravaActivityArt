package com.company.athleteapiart.presentation.format_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Checkbox
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.company.athleteapiart.data.ActivitiesFormat
import com.company.athleteapiart.presentation.composable.*
import com.company.athleteapiart.presentation.destinations.ActivitiesScreenDestination
import com.company.athleteapiart.presentation.destinations.TimeSelectScreenDestination
import com.company.athleteapiart.ui.theme.Black
import com.company.athleteapiart.ui.theme.StravaOrange
import com.company.athleteapiart.ui.theme.WarmGrey20
import com.company.athleteapiart.ui.theme.WarmGrey40
import com.company.athleteapiart.util.AthleteActivities
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun FormatScreen(
    navigator: DestinationsNavigator,
    viewModel: FormatViewModel = hiltViewModel()
) {
    val bgRed by remember { viewModel.backgroundColorRed }
    val bgGreen by remember { viewModel.backgroundColorGreen }
    val bgBlue by remember { viewModel.backgroundColorBlue }


    Scaffold(
        topBar = {
            ComposableTopBar(
                leftContent = {
                    ComposableReturnButton(onClick = {
                        navigator.navigateUp()
                    })
                },
                rightContent = null
            )
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
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
                    item {
                        ComposableItemContainer {
                            ComposableHeader(
                                text = "Background Color",
                                color = StravaOrange
                            )
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(70.dp)
                                    .padding(10.dp)
                                    .background(
                                        color = Color(
                                            bgRed,
                                            bgGreen,
                                            bgBlue
                                        )
                                    )
                                    .border(
                                        width = 5.dp,
                                        color = Color(1f, 1f, 1f, 0.2f)
                                    )
                            )
                            ComposableRGBSlider(
                                text = "Red",
                                color = Color.Red,
                                value = bgRed.toFloat(),
                                onValueChange = { viewModel.backgroundColorRed.value = it }
                            )
                            ComposableRGBSlider(
                                text = "Green",
                                color = Color(0, 128, 0),
                                value = bgGreen.toFloat(),
                                onValueChange = { viewModel.backgroundColorGreen.value = it }
                            )
                            ComposableRGBSlider(
                                text = "Blue",
                                color = Color.Blue,
                                value = bgBlue.toFloat(),
                                onValueChange = { viewModel.backgroundColorBlue.value = it }
                            )
                        }
                    }
                }
            }
        },
        bottomBar = {
            ComposableLargeButton(
                text = "Visualize Activities",
                onClick = {
                    AthleteActivities.formatting.value = ActivitiesFormat(
                        backgroundColor = Color(bgRed, bgGreen, bgBlue)
                    )
                    navigator.navigate(ActivitiesScreenDestination)
                }
            )
        })
}