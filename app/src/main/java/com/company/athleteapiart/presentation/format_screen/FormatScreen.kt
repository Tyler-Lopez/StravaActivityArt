package com.company.athleteapiart.presentation.format_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Checkbox
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.company.athleteapiart.data.ActivitiesFormat
import com.company.athleteapiart.presentation.composable.*
import com.company.athleteapiart.presentation.destinations.ActivitiesScreenDestination
import com.company.athleteapiart.ui.theme.*
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

    val actRed by remember { viewModel.activityColorRed }
    val actGreen by remember { viewModel.activityColorGreen }
    val actBlue by remember { viewModel.activityColorBlue }

    val conditionallyFormat by remember { viewModel.useConditionalFormatting }

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
                    item {
                        ComposableItemContainer {
                            ComposableHeader(
                                text = "Conditional Formatting",
                                color = StravaOrange
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Checkbox(
                                    checked = viewModel.useConditionalFormatting.value,
                                    onCheckedChange = {
                                        viewModel.useConditionalFormatting.value =
                                            !viewModel.useConditionalFormatting.value
                                    })
                                ComposableParagraph(text = "Use Conditional Formatting?")
                            }
                            ComposableSubtext(
                                text = "E.g. Rather than all activities having the same color, make short runs red and long green",
                                modifier = Modifier.padding(horizontal = 20.dp)
                            )
                            /*
                            Box(
                                modifier = Modifier.height(20.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Spacer(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(2.dp)
                                        .clip(RoundedCornerShape(2.dp))
                                        .background(color = WarmGrey50)
                                )
                            }

                            ComposableParagraph(text = if (conditionallyFormat) "Default Color" else "Activity Color")
                            if (conditionallyFormat) {
                                ComposableSubtext(text = "If ")
                            }
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(70.dp)
                                    .padding(10.dp)
                                    .background(
                                        color = Color(
                                            actRed,
                                            actGreen,
                                            actBlue
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
                                value = actRed.toFloat(),
                                modifier = Modifier.padding(start = 10.dp),
                                onValueChange = { viewModel.activityColorRed.value = it }
                            )
                            ComposableRGBSlider(
                                text = "Green",
                                color = Color(0, 128, 0),
                                value = actGreen.toFloat(),
                                onValueChange = { viewModel.activityColorGreen.value = it }
                            )
                            ComposableRGBSlider(
                                text = "Blue",
                                color = Color.Blue,
                                value = actBlue.toFloat(),
                                onValueChange = { viewModel.activityColorBlue.value = it }
                            )

                             */
                        }
                    }


                    item {
                        Spacer(modifier = Modifier.height(100.dp))
                    }
                }
            }
        },
        bottomBar = {
            ComposableLargeButton(
                text = "Visualize Activities",
                onClick = {
                    AthleteActivities.formatting.value = ActivitiesFormat(
                        backgroundColor = Color(bgRed, bgGreen, bgBlue),
                        conditionallyFormat = conditionallyFormat,
                        activityColor = Color(actRed, actGreen, actBlue)
                    )
                    navigator.navigate(ActivitiesScreenDestination)
                }
            )
        })
}