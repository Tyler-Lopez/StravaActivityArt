package com.activityartapp.domain.useCase.activities

import com.activityartapp.data.cache.ActivitiesCache
import javax.inject.Inject

class GetActivitiesByYearFromMemory @Inject constructor(
    private val cache: ActivitiesCache
) {
    operator fun invoke(year: Int) = cache.cachedActivitiesByYear[year]
}