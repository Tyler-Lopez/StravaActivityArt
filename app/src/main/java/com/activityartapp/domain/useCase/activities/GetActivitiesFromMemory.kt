package com.activityartapp.domain.useCase.activities

import com.activityartapp.data.cache.ActivitiesCache
import com.activityartapp.domain.models.Activity
import com.activityartapp.util.TimeUtils
import javax.inject.Inject

class GetActivitiesFromMemory @Inject constructor(
    private val cache: ActivitiesCache,
    private val timeUtils: TimeUtils
) {
    operator fun invoke(): List<Activity> {
        return cache.cachedActivitiesByYear
            .flatMap { it.value }
            .sortedBy { timeUtils.iso8601StringToUnixSecond(it.iso8601LocalDate) }
    }
}