package com.company.activityart.data.remote.responses

data class Athlete(
    val badge_type_id: Int,
    val bio: String,
    val city: String,
    val country: Any,
    val created_at: String,
    val firstname: String,
    val follower: Any,
    val friend: Any,
    val id: Long,
    val lastname: String,
    val premium: Boolean,
    val profile: String,
    val profile_medium: String,
    val resource_state: Int,
    val sex: String,
    val state: String,
    val summit: Boolean,
    val updated_at: String,
    val username: Any,
    val weight: Double
)