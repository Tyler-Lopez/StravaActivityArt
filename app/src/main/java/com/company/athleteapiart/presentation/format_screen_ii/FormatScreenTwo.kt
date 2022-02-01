package com.company.athleteapiart.presentation.format_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.company.athleteapiart.ui.theme.*
import com.company.athleteapiart.util.AthleteActivities


@Composable
fun FormatScreenTwo(
    navController: NavHostController,
    viewModel: FormatTwoViewModel = hiltViewModel()
) {
    val conditionallyFormat by remember { viewModel.useConditionalFormatting }

    val actRed by remember { viewModel.activityColorRed }
    val actGreen by remember { viewModel.activityColorGreen }
    val actBlue by remember { viewModel.activityColorBlue }


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
                            color = WarmGrey20
                        )

                ) {
                    item {
                        ComposableItemContainer {
                            ComposableHeader(
                                text = if (conditionallyFormat) "Default Activity Color" else "Activity Color",
                                color = StravaOrange
                            )
                            if (conditionallyFormat) {
                                ComposableSubtext(
                                    text = "If not defined by a conditional formatting rule, activities will be the following color",
                                    modifier = Modifier.padding(horizontal = 20.dp)
                                )
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
                    AthleteActivities.formatting.value.activityColor = Color(actRed, actGreen, actBlue)
                    navController.navigate("${Screen.VisualizeActivities}")
                }
            )
        })
}