package com.company.activityart.domain.use_case

import com.company.activityart.domain.use_case.get_activities.GetActivitiesUseCase
import com.company.activityart.domain.use_case.insert_activities.InsertActivitiesUseCase

data class ActivitiesUseCases(
    val getActivitiesUseCase: GetActivitiesUseCase,
    val insertActivitiesUseCase: InsertActivitiesUseCase
)