package com.company.athleteapiart.presentation.format_screen

import android.graphics.Paint
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
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
import com.company.athleteapiart.data.ConditionalFormatRule
import com.company.athleteapiart.data.DistanceCondition
import com.company.athleteapiart.data.DistanceRule
import com.company.athleteapiart.presentation.composable.*
import com.company.athleteapiart.ui.spacing
import com.company.athleteapiart.ui.theme.*
import com.company.athleteapiart.util.AthleteActivities
import com.company.athleteapiart.util.meterToMiles


@Composable
fun FormatScreenFour(
    navController: NavHostController,
    viewModel: FormatScreenFourViewModel = hiltViewModel()
) {


    val actRed by remember { viewModel.activityColorRed }
    val actGreen by remember { viewModel.activityColorGreen }
    val actBlue by remember { viewModel.activityColorBlue }
    val distanceSlider by remember { viewModel.distanceSlider }
    val distanceCondition by remember { viewModel.distanceCondition }
    val currIndex by remember { viewModel.currentRule }

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
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            ComposableParagraph(
                                text = "Condition $currIndex",
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Button(
                                onClick = {

                                },
                                colors = ButtonDefaults.buttonColors(backgroundColor = StravaOrange)
                            ) {
                                ComposableParagraph(text = "Add Condition", color = White)
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
                                                viewModel.distanceCondition.value = condition
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
                                        viewModel.activityColorRed.value = it
                                    }
                                )
                                ComposableRGBSlider(
                                    text = "Green",
                                    color = Color(0, 128, 0),
                                    value = actGreen.toFloat(),
                                    onValueChange = {
                                        viewModel.activityColorGreen.value = it
                                    }
                                )
                                ComposableRGBSlider(
                                    text = "Blue",
                                    color = Color.Blue,
                                    value = actBlue.toFloat(),
                                    onValueChange = {
                                        viewModel.activityColorBlue.value = it
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
                    val currRule = viewModel.rules.first()
                    if (currRule is DistanceRule) {
                        currRule.color = Color(actRed, actGreen, actBlue)
                        currRule.distanceCondition = distanceCondition
                        currRule.conditionValue = distanceSlider.toDouble().meterToMiles()
                        println(currRule.color.toString())
                        println(distanceCondition.toString())
                        println(currRule.conditionValue.toString())
                    }
                    navController.navigate("${Screen.VisualizeActivities}")
                }
            )
        })
}