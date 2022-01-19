package com.company.athleteapiart.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.company.athleteapiart.data.remote.responses.Activity
import java.time.Month
import java.time.Year

@Entity
data class Activities(
    @PrimaryKey
    val monthYear: String,
    val activities: List<Activity>
)