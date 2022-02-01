package com.company.athleteapiart.presentation.format_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Checkbox
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.company.athleteapiart.Screen
import com.company.athleteapiart.data.ActivitiesFormat
import com.company.athleteapiart.presentation.composable.*
import com.company.athleteapiart.ui.theme.*
import com.company.athleteapiart.util.AthleteActivities

@Composable
fun FormatScreenOne(
    navController: NavHostController,
    viewModel: FormatOneViewModel = hiltViewModel()
) {
    val bgRed by remember { viewModel.backgroundColorRed }
    val bgGreen by remember { viewModel.backgroundColorGreen }
    val bgBlue by remember { viewModel.backgroundColorBlue }

    val conditionallyFormat by remember { viewModel.useConditionalFormatting }

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
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.9f)
                        .padding(vertical = 10.dp, horizontal = 10.dp)
                        .background(WarmGrey40)
                        .border(
                            width = 2.dp,
                            color = WarmGrey20,
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
                text = "Continue",
                onClick = {
                    AthleteActivities.formatting.value = ActivitiesFormat(
                        backgroundColor = Color(bgRed, bgGreen, bgBlue),
                        conditionallyFormat = conditionallyFormat
                    )
                    navController.navigate("${Screen.FormatActivitiesTwo}")
                }
            )
        })
}