package com.activityartapp.util

import com.activityartapp.domain.models.Activity
import com.activityartapp.util.enums.EditArtSortDirectionType
import com.activityartapp.util.enums.EditArtSortType
import javax.inject.Inject

class ActivitySortUtils @Inject constructor(
    private val timeUtils: TimeUtils
) {
    fun sortActivities(
        activities: List<Activity>,
        sortType: EditArtSortType,
        sortDirection: EditArtSortDirectionType
    ): List<Activity> {
        return activities
            .sortedWith(compareBy {
                when (sortType) {
                    EditArtSortType.DATE -> timeUtils.iso8601StringToUnixSecond(it.iso8601LocalDate)
                    EditArtSortType.DISTANCE -> it.distance
                    EditArtSortType.TYPE -> it.type
                }
            })
            .run {
                if (sortDirection == EditArtSortDirectionType.ASCENDING) {
                    this
                } else {
                    asReversed()
                }
            }
    }
}