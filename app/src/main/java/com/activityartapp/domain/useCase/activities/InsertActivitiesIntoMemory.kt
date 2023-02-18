package com.activityartapp.domain.useCase.activities

import com.activityartapp.data.cache.ActivitiesCache
import com.activityartapp.domain.models.Activity
import javax.inject.Inject

class InsertActivitiesIntoMemory @Inject constructor(private val cache: ActivitiesCache) {
    operator fun invoke(activities: List<Activity>) {
        cache.cachedActivities = activities
    }
}