package com.company.activityart.util

import com.company.activityart.domain.models.Activity
import javax.inject.Inject

class ActivityFilterUtils @Inject constructor(
  private val timeUtils: TimeUtils
) {

    fun filterActivities(
        activities: List<Activity>,
        excludeActivityTypes: Set<String>,
        unixSecondsRange: LongProgression
    ) : List<Activity> {
        return activities.filter {
            when {
                !it.activityWithinUnixSeconds(unixSecondsRange) -> false
                excludeActivityTypes.contains(it.type) -> false
                else -> true
            }
        }
    }

    private fun Activity.activityWithinUnixSeconds(range: LongProgression): Boolean {
        return timeUtils.iso8601StringToUnixSecond(iso8601LocalDate) in range
    }
}