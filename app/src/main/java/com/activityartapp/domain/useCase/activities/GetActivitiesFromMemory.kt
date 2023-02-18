package com.activityartapp.domain.useCase.activities

import com.activityartapp.data.cache.ActivitiesCache
import javax.inject.Inject

class GetActivitiesFromMemory @Inject constructor(
    private val cache: ActivitiesCache
) {
    operator fun invoke() = cache.cachedActivities
}