package com.activityartapp.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.activityartapp.data.Converters
import com.activityartapp.domain.models.AthleteCacheDictionary

@Entity
data class AthleteCacheDictionaryEntity(
    @PrimaryKey override val id: Long,
    @TypeConverters(Converters::class) override val lastCachedYearMonth: Map<Int, Int>
) : AthleteCacheDictionary