package com.company.activityart.data.remote.responses

import com.company.activityart.domain.models.Activity
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
    @SerializedName("type")
    override val type: String,
    @SerializedName("start_date_local")
    override val iso8601LocalDate: String,
    @SerializedName("suffer_score")
    override val sufferScore: Int?,

    val achievement_count: Int,
    val athlete: AthleteX,
    val athlete_count: Int,
    val average_cadence: Double,
    val comment_count: Int,
    val commute: Boolean,
    val display_hide_heartrate_option: Boolean,
    val elapsed_time: Int,
    val elev_high: Double,
    val elev_low: Double,
    val end_latlng: List<Double>,
    val external_id: String,
    val flagged: Boolean,
    val from_accepted_tag: Boolean,
    val has_heartrate: Boolean,
    val has_kudoed: Boolean,
    val heartrate_opt_out: Boolean,
    val manual: Boolean,
    val map: Map?,
    val photo_count: Int,
    val pr_count: Int,
    val `private`: Boolean,
    val resource_state: Int,
    val start_date: String,
    val start_latitude: Double,
    val start_latlng: List<Double>,
    val start_longitude: Double,
    val timezone: String,
    val total_elevation_gain: Double,
    val total_photo_count: Int,
    val trainer: Boolean,
    val upload_id: Long,
    val upload_id_str: String,
    val utc_offset: Double,
    val visibility: String,
    val workout_type: Int
) : Activity {

    override val athleteId: Long
        get() = athlete.id

    override val summaryPolyline: String?
        get() = map?.summary_polyline
}