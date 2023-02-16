package com.activityartapp.util

import android.os.Parcelable
import com.activityartapp.domain.models.Activity
import com.activityartapp.util.enums.SportType
import kotlinx.parcelize.Parcelize

@Parcelize
data class ParcelableActivity(
    override val athleteId: Long,
    override val averageSpeed: Double,
    override val distance: Double,
    override val gearId: String?,
    override val id: Long,
    override val iso8601LocalDate: String,
    override val kudosCount: Int,
    override val locationCity: String?,
    override val locationCountry: String?,
    override val locationState: String?,
    override val maxSpeed: Double,
    override val movingTime: Int,
    override val name: String,
    override val sufferScore: Int?,
    override val summaryPolyline: String?,
    override val sportType: SportType
) : Activity, Parcelable
