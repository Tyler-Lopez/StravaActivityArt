package com.company.activityart.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.company.activityart.domain.models.Activity
import com.company.activityart.domain.models.Athlete

@Entity
data class ActivityEntity(
    override val athleteId: Long,
    override val averageSpeed: Double,
    override val distance: Double,
    override val gearId: String?,
    @PrimaryKey
    override val id: Long,
    override val kudosCount: Int,
    override val locationCity: String?,
    override val locationCountry: String?,
    override val locationState: String?,
    override val maxSpeed: Double,
    override val movingTime: Int,
    override val name: String, // "Happy Friday"
    override val sufferScore: Int?,
    override val iso8601LocalDate: String,
    override val summaryPolyline: String?,
    override val type: String, // "Ride"
) : Activity