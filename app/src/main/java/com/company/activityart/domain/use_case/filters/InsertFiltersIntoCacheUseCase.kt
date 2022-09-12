package com.company.activityart.domain.use_case.filters

import com.company.activityart.data.cache.FiltersCache
import javax.inject.Inject

class InsertFiltersIntoCacheUseCase @Inject constructor(
    private val cache: FiltersCache
) {
    operator fun invoke(
        startDateUnixSeconds: Long? = null,
        endDateUnixSeconds: Long? = null
    ) {
        startDateUnixSeconds?.let { cache.startDateUnixSeconds = it }
        endDateUnixSeconds?.let { cache.endDateUnixSeconds = it }
    }
}