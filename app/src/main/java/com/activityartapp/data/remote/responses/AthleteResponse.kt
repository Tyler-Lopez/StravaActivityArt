package com.activityartapp.data.remote.responses

import com.activityartapp.domain.models.Athlete
import com.google.gson.annotations.SerializedName
import kotlin.collections.Map

data class AthleteResponse(
    @SerializedName("id")
    override val id: Long,
    @SerializedName("username")
    override val userName: String?,
    @SerializedName("resource_state")
    override val resourceState: Int?,
    @SerializedName("firstname")
    override val firstName: String?,
    @SerializedName("lastname")
    override val lastName: String?,
    @SerializedName("bio")
    override val bio: String?,
    @SerializedName("city")
    override val city: String?,
    @SerializedName("state")
    override val state: String?,
    @SerializedName("country")
    override val country: String?,
    @SerializedName("sex")
    override val sex: String?,
    @SerializedName("premium")
    override val premium: Boolean?,
    @SerializedName("summit")
    override val summit: Boolean?,
    @SerializedName("created_at")
    override val createdAt: String?,
    @SerializedName("updated_at")
    override val updatedAt: String?,
    @SerializedName("badge_type_id")
    override val badgeTypeId: Int?,
    @SerializedName("weight")
    override val weight: Double?,
    @SerializedName("profile_medium")
    override val profileMedium: String?,
    @SerializedName("profile")
    override val profile: String?,
    @SerializedName("friend")
    override val friend: Boolean?,
    @SerializedName("follower")
    override val follower: Boolean?
) : Athlete {
    override val lastCachedYearMonth: Map<Int, Int>
        get() = mapOf()
}

/**
 *
 * Example Response:
 *
 * "athlete": {
"id": 94894359,
"username": "tylerlopez",
"resource_state": 2,
"firstname": "Tyler",
"lastname": "Lopez",
"bio": "I just walk",
"city": "",
"state": "",
"country": null,
"sex": "M",
"premium": false,
"summit": false,
"created_at": "2021-11-02T22:12:19Z",
"updated_at": "2023-01-20T17:47:10Z",
"badge_type_id": 0,
"weight": 0.0,
"profile_medium": "https://dgalywyr863hv.cloudfront.net/pictures/athletes/94894359/22514957/7/medium.jpg",
"profile": "https://dgalywyr863hv.cloudfront.net/pictures/athletes/94894359/22514957/7/large.jpg",
"friend": null,
"follower": null
}
 */