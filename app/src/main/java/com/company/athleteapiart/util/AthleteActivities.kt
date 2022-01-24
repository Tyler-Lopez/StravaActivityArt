package com.company.athleteapiart.util

import androidx.compose.runtime.mutableStateOf
import com.company.athleteapiart.data.ActivitiesFormat
import com.company.athleteapiart.data.remote.responses.Activity
import com.company.athleteapiart.ui.theme.Black

object AthleteActivities {
    var activities = mutableStateOf<MutableList<Activity>>(mutableListOf())
    var filteredActivities = mutableStateOf<MutableList<Activity>>(mutableListOf())
    var formatting = mutableStateOf(ActivitiesFormat())
    val activitiesByMonth = mutableStateOf<MutableMap<String, List<Activity>>>(mutableMapOf())
}