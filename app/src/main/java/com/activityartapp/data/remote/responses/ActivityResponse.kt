package com.activityartapp.data.remote.responses

import com.activityartapp.domain.models.Activity
import com.activityartapp.util.enums.SportType
import com.google.gson.annotations.SerializedName

data class ActivityResponse(
    @SerializedName("average_speed")
    override val averageSpeed: Double,
    @SerializedName("distance")
    override val distance: Double,
    @SerializedName("gear_id")
    override val gearId: String?,
    @SerializedName("id")
    override val id: Long,
    @SerializedName("kudos_count")
    override val kudosCount: Int,
    @SerializedName("location_city")
    override val locationCity: String?,
    @SerializedName("location_country")
    override val locationCountry: String?,
    @SerializedName("location_state")
    override val locationState: String?,
    @SerializedName("max_speed")
    override val maxSpeed: Double,
    @SerializedName("moving_time")
    override val movingTime: Int,
    @SerializedName("name")
    override val name: String,
    @SerializedName("sport_type")
    val sportTypeRaw: String,
    @SerializedName("start_date_local")
    override val iso8601LocalDate: String,
    @SerializedName("suffer_score")
    override val sufferScore: Int?,
    val athlete: AthleteWithResourceState,
    val map: Map?
) : Activity {
    override val athleteId: Long
        get() = athlete.id

    override val summaryPolyline: String?
        get() = map?.summary_polyline

    override val sportType: SportType
        get() = SportType.fromSportTypeString(sportTypeRaw)
}