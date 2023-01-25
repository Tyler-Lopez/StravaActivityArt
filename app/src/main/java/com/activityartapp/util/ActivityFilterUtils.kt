package com.activityartapp.util

import android.util.Range
import com.activityartapp.domain.models.Activity
import com.activityartapp.presentation.editArtScreen.DateSelection
import com.activityartapp.presentation.editArtScreen.EditArtViewModel
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.math.roundToInt

class ActivityFilterUtils @Inject constructor(
    private val timeUtils: TimeUtils
) {

    companion object {
        private const val DEFAULT_ACTIVITY_TYPE_SELECTION = true
    }

    fun filterActivities(
        activities: List<Activity>,
        includeActivityTypes: Collection<String>,
        unixMsRange: LongProgression,
        distanceRange: IntRange
    ): List<Activity> {
        println(unixMsRange)
        return activities.filter {
            activityWithinDistanceRange(it, distanceRange) &&
                    activityWithinUnixMs(it, unixMsRange) &&
                    activityTypeContainedWithinTypes(it, includeActivityTypes)
        }
    }

    fun activityWithinDistanceRange(
        activity: Activity,
        range: IntRange
    ): Boolean = activity.distance.roundToInt() in range

    fun activityWithinUnixMs(
        activity: Activity,
        range: LongProgression
    ): Boolean {
        val calcUnix =
            TimeUnit.SECONDS.toMillis(timeUtils.iso8601StringToUnixSecond(activity.iso8601LocalDate))
        return calcUnix >= range.first && calcUnix <= range.last
    }

    fun activityTypeContainedWithinTypes(
        activity: Activity,
        types: Collection<String>
    ): Boolean = types.contains(activity.type)

    /**
     * Provided a [List] of [Activity], returns all possible [DateSelection]. Returns null
     * if empty. If the list is not empty, this method will return a list containing
     * [DateSelection.All], a [DateSelection.Year] for each distinct year, and [DateSelection.Custom].
     *
     * Attempts to retain [customRangeSelectedPreviousMs] into the new [DateSelection.Custom] object.
     **/
    fun getPossibleDateSelections(
        activities: List<Activity>,
        customRangeSelectedPreviousMs: LongProgression?
    ): List<DateSelection>? {
        val unixMs = activities.map {
            TimeUnit.SECONDS.toMillis(timeUtils.iso8601StringToUnixSecond(it.iso8601LocalDate))
        }
        return unixMs
            .maxOrNull()
            ?.let { unixMs.minOrNull()?.rangeTo(it) }
            ?.let { range ->
                mutableListOf<DateSelection>(DateSelection.All).apply {
                    addAll(
                        activities
                            .map { timeUtils.iso8601StringToYear(it.iso8601LocalDate) }
                            .distinct()
                            .map { DateSelection.Year(it) }
                            .toMutableList<DateSelection>()
                            .apply {
                                val adjStart = customRangeSelectedPreviousMs
                                    ?.first
                                    ?.takeIf { it >= range.first }
                                val adjEnd = customRangeSelectedPreviousMs
                                    ?.last
                                    ?.takeIf { it <= range.last }
                                add(
                                    DateSelection.Custom(
                                        dateSelectedStartUnixMs = adjStart ?: range.first,
                                        dateSelectedEndUnixMs = adjEnd ?: range.last,
                                        dateTotalStartUnixMs = range.first,
                                        dateTotalEndUnixMs = range.last
                                    )
                                )
                            })
                }
            }
    }

    fun getPossibleActivityTypes(
        activities: List<Activity>,
        filterTypesPrevious: Map<String, Boolean>?
    ): Map<String, Boolean>? {
        return activities
            .takeIf { it.isNotEmpty() }
            ?.distinctBy(Activity::type)
            ?.map(Activity::type)
            ?.sorted()
            ?.associateWith {
                filterTypesPrevious?.get(it) ?: DEFAULT_ACTIVITY_TYPE_SELECTION
            }
    }

    fun getPossibleDistances(
        activities: List<Activity>
    ): ClosedFloatingPointRange<Double>? {
        val distances =activities.map { it.distance }
        val distanceShortest = distances.minOrNull()
        val distanceLongest = distances.maxOrNull()
        return distanceLongest?.let { distanceShortest?.rangeTo(it) }
    }
}