package com.activityartapp.domain.use_case.activities

import com.activityartapp.data.cache.ActivitiesCache
import com.activityartapp.domain.models.Activity
import com.activityartapp.util.TimeUtils
import javax.inject.Inject

class GetActivitiesFromCacheUseCase @Inject constructor(
    private val cache: ActivitiesCache,
    private val timeUtils: TimeUtils
) {
    operator fun invoke(): List<Activity> {
        return cache.cachedActivitiesByYear
            .flatMap { it.value }
            .sortedBy { timeUtils.iso8601StringToUnixSecond(it.iso8601LocalDate) }
    }
}