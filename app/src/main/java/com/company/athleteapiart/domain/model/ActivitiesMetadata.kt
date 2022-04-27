package com.company.athleteapiart.domain.model

import com.company.athleteapiart.data.remote.responses.Activity

data class ActivitiesMetadata(
    val activityTypes: Set<String>, // Run, Cycle, Climb
  //  val gearTypes: Set<String>,
    val months: Set<String>,
)