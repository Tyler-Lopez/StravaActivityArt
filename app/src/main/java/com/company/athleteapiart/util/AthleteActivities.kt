package com.company.athleteapiart.util

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.company.athleteapiart.domain.model.ActivitiesFormat
import com.company.athleteapiart.data.remote.responses.Activity

object AthleteActivities {
    var currYear: MutableState<Int> = mutableStateOf(0);
    var activities = mutableStateOf<MutableList<Activity>>(mutableListOf())
    var filteredActivities = mutableStateOf<MutableList<Activity>>(mutableListOf())
    var formatting = mutableStateOf(ActivitiesFormat())
    val activitiesByMonth = mutableStateOf<MutableMap<String, List<Activity>>>(mutableMapOf())
}