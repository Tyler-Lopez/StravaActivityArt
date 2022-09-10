package com.company.activityart.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import com.company.activityart.util.navtype.ActivitiesNavType

/**
 * Implementation of [Activity] which is Parcelable and thus may be packaged
 * in [Activities] and put into a bundle as the [ActivitiesNavType].
 */
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
    override val type: String
) : Activity, Parcelable
