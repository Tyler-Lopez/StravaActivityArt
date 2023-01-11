package com.activityartapp.util

import com.activityartapp.domain.models.Activity
import javax.inject.Inject

class ActivityFilterUtils @Inject constructor(
  private val timeUtils: TimeUtils
) {

    fun filterActivities(
        activities: List<Activity>,
        includeActivityTypes: Set<String>,
        unixSecondsRange: LongProgression,
        distanceRange: ClosedFloatingPointRange<Double>
    ) : List<Activity> {
        return activities.filter {
            when {
                !it.activityWithinUnixSeconds(unixSecondsRange) -> false
                it.distance !in distanceRange -> false
                !includeActivityTypes.contains(it.type) -> {
                    false
                }
                else -> true
            }
        }
    }

    private fun Activity.activityWithinUnixSeconds(range: LongProgression): Boolean {
        val calcUnix = timeUtils.iso8601StringToUnixSecond(iso8601LocalDate)
        return calcUnix >= range.first && calcUnix <= range.last
    }
}