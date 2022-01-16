package com.company.athleteapiart.util

import androidx.compose.runtime.mutableStateOf
import com.company.athleteapiart.data.remote.responses.Activity

object AthleteActivities {
    var activities = mutableStateOf<MutableList<Activity>>(mutableListOf())
    var selectedActivities = mutableStateOf<List<Activity>>(listOf())
    // var selectedActivity = mutableStateOf<Activity?>(null)
}