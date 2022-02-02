package com.company.athleteapiart.presentation.format_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Checkbox
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
import androidx.navigation.NavHostController
import com.company.athleteapiart.Screen
import com.company.athleteapiart.data.ActivitiesFormat
import com.company.athleteapiart.presentation.composable.*
import com.company.athleteapiart.ui.spacing
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
            ComposableTopBar(
                leftContent = {
                    ComposableReturnButton {
                        navController.navigateUp()
                    }
                },
                rightContent = {
                    ComposableParagraph(
                        text = "Format",
                        color = Color.White
                    )
                }
            )
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ComposableScreenWrapper(
                    // Create room for large button
                    modifier = Modifier.padding(bottom = 75.dp)
                ) {
                    ComposableShadowBox {
                        Column {
                            ComposableItemContainer {
                                ComposableHeader(
                                    text = "Background Color",
                                    color = StravaOrange,
                                    isBold = true,
                                    modifier = Modifier.padding(bottom = MaterialTheme.spacing.sm)
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
                            ComposableItemContainer(
                                modifier = Modifier.padding(vertical = MaterialTheme.spacing.sm)
                            ) {
                                ComposableHeader(
                                    text = "Conditional Formatting",
                                    color = StravaOrange,
                                    isBold = true,
                                    modifier = Modifier.padding(bottom = MaterialTheme.spacing.sm)
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
                            Spacer(
                                modifier = Modifier.height(250.dp)
                            )
                        }
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