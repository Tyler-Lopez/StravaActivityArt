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


@Composable
fun FormatScreenFour(
    navController: NavHostController,
    viewModel: FormatScreenFourViewModel = hiltViewModel()
) {


    val actRed by remember { viewModel.activityColorRed }
    val actGreen by remember { viewModel.activityColorGreen }
    val actBlue by remember { viewModel.activityColorBlue }

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
                                text = "Condition 1",
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
                                    text = "Condition Color",
                                    color = StravaOrange,
                                    isBold = true,
                                    modifier = Modifier.padding(start = MaterialTheme.spacing.xxs)
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
                    AthleteActivities.formatting.value.conditions.first().color =
                        Color(actRed, actGreen, actBlue)
                    navController.navigate("${Screen.VisualizeActivities}")
                }
            )
        })
}