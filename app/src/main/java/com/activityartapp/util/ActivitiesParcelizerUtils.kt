package com.activityartapp.util

import com.activityartapp.domain.models.Activity

fun List<Activity>.parcelize(): List<ParcelableActivity> {
    return map {
        ParcelableActivity(
            it.athleteId,
            it.averageSpeed,
            it.distance,
            it.gearId,
            it.id,
            it.iso8601LocalDate,
            it.kudosCount,
            it.locationCity,
            it.locationCountry,
            it.locationState,
            it.maxSpeed,
            it.movingTime,
            it.name,
            it.sufferScore,
            it.summaryPolyline,
            it.sportType
        )
    }
}