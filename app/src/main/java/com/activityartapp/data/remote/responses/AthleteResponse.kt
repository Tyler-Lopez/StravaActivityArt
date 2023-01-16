package com.activityartapp.data.remote.responses

import com.activityartapp.domain.models.Athlete
import com.google.gson.annotations.SerializedName
import kotlin.collections.Map

data class AthleteResponse(
    @SerializedName("id")
    override val athleteId: Long,
    /* TODO COMMENTED FOR NOW
    @SerializedName("username")
    override val userName: String,
    @SerializedName("firstname")
    override val firstName: String,
    @SerializedName("lastname")
    override val lastName: String,
    @SerializedName("profile")
    override val profilePictureLarge: String,
    @SerializedName("profile_medium")
    override val profilePictureMedium: String,

     */
    val athlete_type: Int,
    val badge_type_id: Int,
    val bikes: List<Bike>?,
    val city: String,
    val clubs: List<Any>,
    val country: String,
    val created_at: String,
    val date_preference: String,
    val follower: Any,
    val follower_count: Int,
    val friend: Any,
    val friend_count: Int,
    val ftp: Any,
    val measurement_preference: String,
    val mutual_friend_count: Int,
    val premium: Boolean,
    val resource_state: Int,
    val sex: String,
    val shoes: List<Shoe>?,
    val state: String,
    val updated_at: String,
    val weight: Int
) : Athlete {

    // TODO COMMENTED FOR NOW override val receivedOnUnixSeconds: Int? = null

    override val lastCachedYearMonth: Map<Int, Int>
        get() = mapOf()
    //override val yearMonthsCached: Map<Int, Int> = mapOf()
    //override val gears: Map<String, String> = mapOf()
}