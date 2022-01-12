package com.company.athleteapiart.presentation.activity_select_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.company.athleteapiart.presentation.activity_select_screen.composable.ComposableActivityDetail
import com.company.athleteapiart.presentation.athletescreen.ActivityScreen
import com.company.athleteapiart.util.AthleteActivities
import com.company.athleteapiart.util.formatIso8601

@Composable
fun ActivitySelectScreen(
    viewModel: ActivitySelectViewModel = hiltViewModel(),
    onActivitySelect: () -> Unit
) {
    val activities by remember { AthleteActivities.activities }
    val loadError by remember { viewModel.loadError }
    val isLoading by remember { viewModel.isLoading }
    Surface(color = Color.LightGray) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (isLoading) {
                Text("Loading Activities")
            } else {
                LazyColumn {
                    items(activities) { activity ->
                        ComposableActivityDetail(
                            activity = activity,
                            modifier = Modifier
                                .fillMaxWidth()
                                .shadow(2.dp)
                                .background(Color.White)
                                .clickable {
                                    AthleteActivities.selectedActivity.value = activity
                                    onActivitySelect()
                                }

                        )
                        Spacer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(5.dp)
                        )

                    }
                }
            }
        }
    }
}