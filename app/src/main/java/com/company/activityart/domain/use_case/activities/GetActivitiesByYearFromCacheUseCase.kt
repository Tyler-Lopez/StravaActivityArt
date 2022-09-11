package com.company.activityart.domain.use_case.activities

import com.company.activityart.data.cache.ActivitiesCache
import javax.inject.Inject

class GetActivitiesByYearFromCacheUseCase @Inject constructor(
    private val cache: ActivitiesCache
) {
    operator fun invoke(year: Int) = cache.cachedActivitiesByYear[year]
}