package com.company.athleteapiart.presentation.filter_activities_screen

import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.company.athleteapiart.presentation.composable.*
import com.company.athleteapiart.presentation.destinations.ActivitiesScreenDestination
import com.company.athleteapiart.presentation.destinations.TimeSelectScreenDestination
import com.company.athleteapiart.ui.theme.*
import com.company.athleteapiart.util.AthleteActivities
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun FilterActivitiesScreen(
    navigator: DestinationsNavigator,
    viewModel: FilterActivitiesViewModel = hiltViewModel()
) {
    val isLoading by remember { viewModel.isLoading }

    Scaffold(
        topBar = {
            ComposableTopBar(
                leftContent = {
                    ComposableReturnButton(onClick = {
                        navigator.navigate(
                            TimeSelectScreenDestination
                        )
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
                when {
                    isLoading -> {
                        CircularProgressIndicator(
                            color = MaterialTheme.colors.primary
                        )
                        Row(
                            modifier = Modifier
                                .padding(vertical = 10.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.Bottom,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            ComposableHeader(
                                text = "Processing",
                                isBold = true
                            )
                        }
                    }
                    else -> {
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
                                        text = "Months",
                                        color = StravaOrange
                                    )
                                    for (month in viewModel.months.reversed()) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Checkbox(
                                                checked = viewModel
                                                    .checkboxes
                                                    .getOrDefault(
                                                        month,
                                                        false
                                                    ),
                                                onCheckedChange = {
                                                    viewModel.flipValue(month)
                                                })
                                            ComposableParagraph(text = month)
                                        }
                                    }
                                }
                            }

                            item {
                                ComposableItemContainer {
                                    ComposableHeader(
                                        text = "Activity Types",
                                        color = StravaOrange
                                    )
                                    for (activity in viewModel.activityTypes) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Checkbox(
                                                checked = viewModel
                                                    .checkboxes
                                                    .getOrDefault(
                                                        activity,
                                                        false
                                                    ),
                                                onCheckedChange = {
                                                    viewModel.flipValue(activity)
                                                })
                                            ComposableParagraph(text = activity)
                                        }
                                    }
                                }
                            }
                            item {
                                ComposableItemContainer {
                                    ComposableHeader(
                                        text = "Distance",
                                        color = StravaOrange
                                    )
                                    ComposableParagraph(text = "Minimum Distance")
                                    Slider(
                                        value = 10f,
                                        onValueChange = {

                                        },
                                        valueRange = 0f..100f
                                    )
                                    ComposableParagraph(text = "Maximum Distance")
                                    Slider(
                                        value = 10f,
                                        onValueChange = {

                                        },
                                        valueRange = 0f..100f,
                                        modifier = Modifier.drawBehind {
                                            val incrementWidth =
                                                (this.size.width - 10.dp.toPx()) / 10f
                                            for (i in 0..10) {
                                                drawLine(
                                                    color = StravaOrange,
                                                    start = Offset(
                                                        x = 5.dp.toPx() + i * incrementWidth,
                                                        y = this.center.y - 10f
                                                    ),
                                                    end = Offset(
                                                        x = 5.dp.toPx() + i * incrementWidth,
                                                        y = this.center.y + 10f
                                                    ),
                                                    cap = StrokeCap.Round,
                                                    strokeWidth = 2f
                                                )
                                                drawIntoCanvas {
                                                    val stroke = Paint()
                                                    stroke.textAlign = Paint.Align.CENTER
                                                    stroke.textSize = 25f
                                                    stroke.color = Color.rgb(148, 148, 148)
                                                    stroke.typeface = Typeface.create("Arial", Typeface.NORMAL)

                                                    it.nativeCanvas.drawText(
                                                        i.toString(),
                                                        5.dp.toPx() + i * incrementWidth,
                                                        this.center.y + 40f,
                                                        stroke
                                                    )
                                                }
                                            }
                                        }
                                    )
                                    Spacer(modifier = Modifier.height(20.dp))
                                }
                            }
                            item {
                                Spacer(
                                    modifier = Modifier.height(250.dp)
                                )
                            }
                        }
                    }
                }
            }
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(75.dp),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 10.dp, start = 20.dp, end = 20.dp),
                    onClick = {
                        AthleteActivities.selectedActivities =
                            AthleteActivities.activities.value
                        navigator.navigate(ActivitiesScreenDestination)
                    }
                ) {
                    ComposableHeader(
                        text = "Apply Filters",
                        color = White
                    )
                }
            }
        })
}