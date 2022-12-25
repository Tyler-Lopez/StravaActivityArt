package com.company.activityart.util

import com.company.activityart.domain.models.Activity
import javax.inject.Inject

class ActivityFilterUtils @Inject constructor(
  private val timeUtils: TimeUtils
) {

    fun filterActivities(
        activities: List<Activity>,
        includeActivityTypes: Set<String>,
        unixSecondsRange: LongProgression
    ) : List<Activity> {
        return activities.filter {
            when {
                !it.activityWithinUnixSeconds(unixSecondsRange) -> {
                    println("false based on seconds")
                    false
                }
                !includeActivityTypes.contains(it.type) -> {
                    false
                }
                else -> true
            }
        }
    }

    private fun Activity.activityWithinUnixSeconds(range: LongProgression): Boolean {
        val calcUnix = timeUtils.iso8601StringToUnixSecond(iso8601LocalDate)
        println("calc  unix is $calcUnix and range is $range")
        return calcUnix >= range.first && calcUnix <= range.last
    }
}