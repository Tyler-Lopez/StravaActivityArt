package com.company.athleteapiart.presentation.activity_select_screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
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

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isLoading) {
            Text("Loading Activities")
        } else {
            activities.forEach {
                Button(
                    modifier = Modifier
                        .fillMaxWidth(),
                    onClick = {
                        AthleteActivities.selectedId = it.id
                        onActivitySelect()
                }) {
                    Row(modifier = Modifier
                        .fillMaxWidth()
                    ) {
                        Text(it.name)
                        Text(it.start_date_local.formatIso8601(), color = Color.Cyan)
                        Text("${it.distance}", color = Color.Green)
                        Text("${it.moving_time}", color = Color.Red)
                    }
                }
            }
        }
    }
}