package com.company.athleteapiart.presentation.format_screen

import androidx.compose.foundation.layout.*
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
import com.company.athleteapiart.presentation.composable.*
import com.company.athleteapiart.ui.spacing
import com.company.athleteapiart.ui.theme.*
import com.company.athleteapiart.util.AthleteActivities


@Composable
fun FormatScreenThree(
    navController: NavHostController,
    viewModel: FormatScreenThreeViewModel = hiltViewModel()
) {
    val conditionallyFormat by remember { viewModel.useConditionalFormatting }

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
            //        modifier = Modifier.padding(bottom = 75.dp)
                ) {
                    ComposableShadowBox {
                        Column {
                            ComposableItemContainer {
                                ComposableHeader(
                                    text = if (conditionallyFormat) "Default Activity Color" else "Activity Color",
                                    color = StravaOrange,
                                    isBold = true,
                                    modifier = Modifier.padding(start = MaterialTheme.spacing.xxs)
                                )
                                if (conditionallyFormat) {
                                    ComposableSubtext(
                                        text = "If not defined by a conditional formatting rule, activities will be the following color",
                                        modifier = Modifier.padding(
                                            horizontal = MaterialTheme.spacing.xxs,
                                            vertical = MaterialTheme.spacing.sm
                                        )
                                    )
                                }
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
                    AthleteActivities.formatting.value.activityColor =
                        Color(actRed, actGreen, actBlue)
                    navController.navigate(
                        if (conditionallyFormat)
                            "${Screen.FormatActivitiesFour}"
                        else "${Screen.VisualizeActivities}"
                    )
                }
            )
        })
}