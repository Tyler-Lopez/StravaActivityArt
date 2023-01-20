package com.activityartapp.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.activityartapp.data.Converters
import com.activityartapp.domain.models.Athlete

@Entity
data class AthleteEntity(
    @PrimaryKey
    override val id: Long,
    override val userName: String?,
    override val resourceState: Int?,
    override val firstName: String?,
    override val lastName: String?,
    override val bio: String?,
    override val city: String?,
    override val state: String?,
    override val country: String?,
    override val sex: String?,
    override val premium: Boolean?,
    override val summit: Boolean?,
    override val createdAt: String?,
    override val updatedAt: String?,
    override val badgeTypeId: Int?,
    override val weight: Double?,
    override val profileMedium: String?,
    override val profile: String?,
    override val friend: Boolean?,
    override val follower: Boolean?,
    @TypeConverters(Converters::class)
    override val lastCachedYearMonth: Map<Int, Int>
) : Athlete