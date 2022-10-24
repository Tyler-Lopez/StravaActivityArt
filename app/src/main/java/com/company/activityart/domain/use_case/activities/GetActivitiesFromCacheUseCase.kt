package com.company.activityart.domain.use_case.activities

import com.company.activityart.data.cache.ActivitiesCache
import com.company.activityart.domain.models.Activity
import com.company.activityart.util.TimeUtils
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