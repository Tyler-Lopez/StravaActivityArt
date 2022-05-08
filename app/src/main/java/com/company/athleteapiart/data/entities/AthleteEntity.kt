package com.company.athleteapiart.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AthleteEntity(
    @PrimaryKey
    val athleteId: Long,
    val userName: String,
    val receivedOn: Int,
    val firstName: String,
    val profileMedium: String,
    val datePreference: String?,
    val lastName: String
)
