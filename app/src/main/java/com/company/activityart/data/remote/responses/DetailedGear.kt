package com.company.activityart.data.remote.responses

data class DetailedGear(
    val brand_name: String,
    val description: String,
    val distance: Int,
    val frame_type: Int,
    val id: String,
    val model_name: String,
    val primary: Boolean,
    val resource_state: Int
)