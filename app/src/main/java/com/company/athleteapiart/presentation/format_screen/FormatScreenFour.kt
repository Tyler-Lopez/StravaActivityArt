package com.company.athleteapiart.presentation.format_screen

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.*
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
import com.company.athleteapiart.data.DistanceCondition
import com.company.athleteapiart.presentation.composable.*
import com.company.athleteapiart.ui.spacing
import com.company.athleteapiart.ui.theme.*
import com.company.athleteapiart.util.meterToMiles


@Composable
fun FormatScreenFour(
    navController: NavHostController,
    viewModel: FormatScreenFourViewModel = hiltViewModel()
) {


    val actRed by remember { viewModel.red }
    val actGreen by remember { viewModel.green }
    val actBlue by remember { viewModel.blue }
    val distanceSlider by remember { viewModel.distanceSlider }
    val distanceCondition by remember { viewModel.distanceCondition }
    val currIndex by remember { viewModel.currRule }
    val rulesSize by remember { viewModel.rulesSize }


    Scaffold(
        topBar = {
            ComposableTopBar(
                leftContent = {
                    ComposableReturnButton {
                        navController.navigateUp()
                    }
                },
                rightContent = {
                    ComposableSubtext(
                        text = "Conditions",
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
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = MaterialTheme.spacing.md)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .horizontalScroll(
                                    state = rememberScrollState()
                                )
                        ) {
                            for (i in 0 until rulesSize) {
                                Button(onClick = {
                                    viewModel.setCurrRule(i)
                                }) {
                                    ComposableParagraph(
                                        text = "Condition $i" + if (viewModel.currRule.value == i) "*" else "-",
                                    )
                                }
                            }
                            Button(
                                onClick = {
                                    viewModel.incrementRule()
                                },
                                colors = ButtonDefaults.buttonColors(backgroundColor = StravaOrange)
                            ) {
                                ComposableParagraph(text = "+", color = White)
                            }
                        }
                    }
                    ComposableShadowBox {
                        Column {
                            ComposableItemContainer {
                                ComposableHeader(
                                    text = "Condition",
                                    color = StravaOrange,
                                    isBold = true,
                                    modifier = Modifier.padding(
                                        start = MaterialTheme.spacing.xxs,
                                        bottom = MaterialTheme.spacing.sm
                                    )
                                )
                                for (condition in DistanceCondition.values()) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        RadioButton(
                                            selected = distanceCondition == condition,
                                            onClick = {
                                                viewModel.setDistanceCondition(condition)
                                            })
                                        ComposableParagraph(
                                            text = condition.toString(),
                                            modifier = Modifier.padding(end = MaterialTheme.spacing.sm)
                                        )
                                        ComposableParagraph(
                                            text = "%.1f"
                                                .format(
                                                    (distanceSlider)
                                                        .toDouble()
                                                        .meterToMiles()
                                                ) + " mi",
                                            color = StravaOrange
                                        )
                                    }

                                }
                                val valueRange =
                                    0f..viewModel.maxDistance.value
                                Slider(
                                    value = distanceSlider,
                                    onValueChange = {
                                        viewModel.setDistanceSlider(it)
                                    },
                                    valueRange = valueRange,
                                    modifier = Modifier.padding(horizontal = 10.dp)
                                )
                            }
                            ComposableItemContainer(modifier = Modifier.padding(vertical = MaterialTheme.spacing.sm)) {
                                ComposableHeader(
                                    text = "Condition Color",
                                    color = StravaOrange,
                                    isBold = true,
                                    modifier = Modifier.padding(
                                        start = MaterialTheme.spacing.xxs
                                    )
                                )
                                ComposableColorBox(
                                    color = Color(
                                        actRed,
                                        actGreen,
                                        actBlue
                                    )
                                )
                                ComposableRGBSlider(
                                    text = "Red",
                                    color = Color.Red,
                                    value = actRed.toFloat(),
                                    modifier = Modifier.padding(start = 10.dp),
                                    onValueChange = {
                                        viewModel
                                            .changeColor(
                                                FormatScreenFourViewModel.ColorChoice.RED,
                                                it
                                            )
                                    }
                                )
                                ComposableRGBSlider(
                                    text = "Green",
                                    color = Color(0, 128, 0),
                                    value = actGreen.toFloat(),
                                    onValueChange = {
                                        viewModel
                                            .changeColor(
                                                FormatScreenFourViewModel.ColorChoice.GREEN,
                                                it
                                            )
                                    }
                                )
                                ComposableRGBSlider(
                                    text = "Blue",
                                    color = Color.Blue,
                                    value = actBlue.toFloat(),
                                    onValueChange = {
                                        viewModel
                                            .changeColor(
                                                FormatScreenFourViewModel.ColorChoice.BLUE,
                                                it
                                            )
                                    }
                                )
                            }
                        }
                    }
                }
            }
        },
        bottomBar = {
            ComposableLargeButton(
                text = "Continue",
                onClick = {
                    navController.navigate("${Screen.VisualizeActivities}")
                }
            )
        })
}