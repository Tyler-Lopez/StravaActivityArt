package com.company.activityart.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.company.activityart.domain.models.Activity
import com.company.activityart.domain.models.Athlete

@Entity(primaryKeys = ["id"])
data class ActivityEntity(
    override val athleteId: Long,
    override val averageSpeed: Double,
    override val distance: Double,
    override val gearId: String?,
    override val id: Long,
    override val kudosCount: Int,
    override val locationCity: String?,
    override val locationCountry: String?,
    override val locationState: String?,
    override val maxSpeed: Double,
    override val month: Int,
    override val movingTime: Int,
    override val name: String, // "Happy Friday"
    override val sufferScore: Int?,
    override val summaryPolyline: String?,
    override val type: String, // "Ride"
    override val year: Int
) : Activity