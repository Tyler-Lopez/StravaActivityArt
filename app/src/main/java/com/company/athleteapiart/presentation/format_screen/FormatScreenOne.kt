package com.company.athleteapiart.presentation.format_screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
    viewModel: FormatScreenOneViewModel = hiltViewModel()
) {
    val bgRed by remember { viewModel.backgroundColorRed }
    val bgGreen by remember { viewModel.backgroundColorGreen }
    val bgBlue by remember { viewModel.backgroundColorBlue }


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
                        text = "Format",
                        color = Color.White
                    )
                }
            )
        },
        content = {
            Column {
                ComposableScreenWrapper(
                    // Create room for large button
               //     modifier = Modifier.padding(bottom = 75.dp)
                ) {
                    ComposableShadowBox {
                        Column {
                            ComposableItemContainer {
                                ComposableHeader(
                                    text = "Background Color",
                                    color = StravaOrange,
                                    isBold = true,
                                    modifier = Modifier.padding(start = MaterialTheme.spacing.xxs)
                                )
                                ComposableColorBox(
                                    color = Color(
                                        bgRed,
                                        bgGreen,
                                        bgBlue
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
            }
        },
        bottomBar = {
            ComposableLargeButton(
                text = "Continue",
                onClick = {
                    AthleteActivities.formatting.value = ActivitiesFormat(
                        backgroundColor = Color(bgRed, bgGreen, bgBlue),
                    )
                    navController.navigate("${Screen.FormatActivitiesTwo}")
                }
            )
        })
}