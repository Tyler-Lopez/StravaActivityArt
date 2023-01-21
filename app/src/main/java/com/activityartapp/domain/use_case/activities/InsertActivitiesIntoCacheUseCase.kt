package com.activityartapp.domain.use_case.activities

import com.activityartapp.data.cache.ActivitiesCache
import com.activityartapp.domain.models.Activity
import javax.inject.Inject

class InsertActivitiesIntoCacheUseCase @Inject constructor(
    private val cache: ActivitiesCache
) {
    operator fun invoke(year: Int, activities: List<Activity>) {
        cache.cachedActivitiesByYear[year] = activities
    }
}