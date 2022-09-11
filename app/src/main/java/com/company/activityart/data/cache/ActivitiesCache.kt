package com.company.activityart.data.cache

import com.company.activityart.domain.models.Activity

/**
 * Global Singleton cache data-layer which is populated during the first data load and
 * then never again.
 */
object ActivitiesCache {
    val cachedActivitiesByYear: MutableMap<Int, List<Activity>> = mutableMapOf()
}