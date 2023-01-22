package com.activityartapp.data.remote.responses

import com.activityartapp.domain.models.Athlete

data class  AthleteWithResourceState(
    override val id: Long,
    val resource_state: Int
) : Athlete