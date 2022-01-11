package com.company.athleteapiart.util

import androidx.compose.runtime.mutableStateOf
import com.company.athleteapiart.data.remote.responses.Activity

object AthleteActivities {
    var activities = mutableStateOf<List<Activity>>(listOf())
    var selectedId: Long = 0L
}