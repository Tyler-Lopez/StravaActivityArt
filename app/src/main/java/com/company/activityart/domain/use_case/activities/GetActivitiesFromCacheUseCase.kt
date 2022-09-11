package com.company.activityart.domain.use_case.activities

import com.company.activityart.data.cache.ActivitiesCache
import com.company.activityart.domain.models.Activity
import javax.inject.Inject

class GetActivitiesFromCacheUseCase @Inject constructor(
    private val cache: ActivitiesCache
) {
    operator fun invoke(): Map<Int, List<Activity>> {
        return cache.cachedActivitiesByYear
    }
}