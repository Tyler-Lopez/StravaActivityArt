package com.activityartapp.data.cache

import com.activityartapp.domain.models.Activity

/**
 * Global Singleton cache data-layer which is populated during the first data load and
 * then never again.
 */
class ActivitiesCache {
    val cachedActivitiesByYear: MutableMap<Int, List<Activity>> = mutableMapOf()
}