package com.company.activityart.data.remote.responses

data class Bearer(
    val access_token: String,
    val athlete: Athlete,
    val expires_at: Int,
    val expires_in: Int,
    val refresh_token: String,
    val token_type: String
)