package com.activityartapp.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.activityartapp.data.remote.responses.ActivityResponse

@Entity
data class Activities(
    @PrimaryKey
    val monthYear: String,
    val activities: List<ActivityResponse>
)