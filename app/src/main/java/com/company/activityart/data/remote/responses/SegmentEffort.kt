package com.company.activityart.data.remote.responses

data class SegmentEffort(
    val achievements: List<Any>,
    val activity: ActivityXX,
    val athlete: AthleteXXXX,
    val average_cadence: Double,
    val average_watts: Double,
    val device_watts: Boolean,
    val distance: Double,
    val elapsed_time: Int,
    val end_index: Int,
    val hidden: Boolean,
    val id: Long,
    val kom_rank: Any,
    val moving_time: Int,
    val name: String,
    val pr_rank: Any,
    val resource_state: Int,
    val segment: Segment,
    val start_date: String,
    val start_date_local: String,
    val start_index: Int
)