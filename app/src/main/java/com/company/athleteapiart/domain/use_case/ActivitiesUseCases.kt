package com.company.athleteapiart.domain.use_case

import com.company.athleteapiart.domain.use_case.get_activities.GetActivitiesUseCase
import com.company.athleteapiart.domain.use_case.insert_activities.InsertActivitiesUseCase

data class ActivitiesUseCases(
    val getActivitiesUseCase: GetActivitiesUseCase,
    val insertActivitiesUseCase: InsertActivitiesUseCase
)