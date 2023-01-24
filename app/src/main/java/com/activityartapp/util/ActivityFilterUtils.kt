package com.activityartapp.util

import com.activityartapp.domain.models.Activity
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.math.roundToInt

class ActivityFilterUtils @Inject constructor(
    private val timeUtils: TimeUtils
) {

    fun filterActivities(
        activities: List<Activity>,
        includeActivityTypes: Collection<String>,
        unixMsRange: LongProgression,
        distanceRange: IntRange
    ): List<Activity> {
        println("invoked filter activities")
        println(unixMsRange)
        return activities.filter {
            val result = activityWithinDistanceRange(it, distanceRange) &&
                    activityWithinUnixMs(it, unixMsRange) &&
                    activityTypeContainedWithinTypes(it, includeActivityTypes)
            println("result was $result")
            result

        }
    }

    fun activityWithinDistanceRange(
        activity: Activity,
        range: IntRange
    ): Boolean {
        return activity.distance.roundToInt() in range
    }

    fun activityWithinUnixMs(
        activity: Activity,
        range: LongProgression
    ): Boolean {
        val calcUnix = TimeUnit.SECONDS.toMillis(timeUtils.iso8601StringToUnixSecond(activity.iso8601LocalDate))
        return calcUnix >= range.first && calcUnix <= range.last
    }

    fun activityTypeContainedWithinTypes(
        activity: Activity,
        types: Collection<String>
    ): Boolean {
        return types.contains(activity.type)
    }
}