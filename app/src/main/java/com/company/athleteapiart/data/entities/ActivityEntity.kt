package com.company.athleteapiart.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ActivityEntity(
    @PrimaryKey
    val activityId: Long,
    val athleteId: Long,
    val activityName: String, // "Happy Friday"
    val activityType: String, // "Ride"
    val activityTimeLocal: String, // "2018-02-16T06:56:25Z"
    val activityDistance: Double,
    val summaryPolyline: String?,
    val locationCity: String?,
    val locationState: String?,
    val locationCountry: String?,
    val maxSpeed: Double,
    val averageSpeed: Double,
    val movingTime: Int,
    val gearId: String?,
    val kudosCount: Int,
)