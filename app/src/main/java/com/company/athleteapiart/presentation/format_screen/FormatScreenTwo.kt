package com.company.athleteapiart.presentation.format_screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
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
fun FormatScreenTwo(
    navController: NavHostController,
    viewModel: FormatScreenTwoViewModel = hiltViewModel()
) {

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

                            ComposableItemContainer(
                                modifier = Modifier.padding(vertical = MaterialTheme.spacing.sm)
                            ) {
                                ComposableHeader(
                                    text = "Conditional Formatting",
                                    color = StravaOrange,
                                    isBold = true,
                                    modifier = Modifier.padding(
                                        start = MaterialTheme.spacing.xxs,
                                    )
                                )
                                ComposableSubtext(
                                    text = "Enable to color activities " +
                                            "according to a property, such as activities colored " +
                                            "from red to green depending on distance",
                                    modifier = Modifier.padding(
                                        horizontal = MaterialTheme.spacing.xxs,
                                        vertical = MaterialTheme.spacing.sm
                                    )
                                )
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Switch(
                                        checked = viewModel.useConditionalFormatting.value,
                                        onCheckedChange = {
                                            viewModel.useConditionalFormatting.value =
                                                !viewModel.useConditionalFormatting.value
                                        },
                                        colors = SwitchDefaults
                                            .colors(
                                                checkedThumbColor = StravaOrange
                                            )
                                    )
                                    ComposableParagraph(
                                        text =
                                        if (viewModel.useConditionalFormatting.value)
                                            "Enabled"
                                        else "Disabled"
                                    )
                                }
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
                    AthleteActivities
                        .formatting
                        .value
                        .conditionallyFormat =
                        viewModel
                            .useConditionalFormatting
                            .value
                    navController.navigate("${Screen.FormatActivitiesThree}")
                }
            )
        })
}