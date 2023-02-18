package com.activityartapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.activityartapp.data.dao.ActivityDao
import com.activityartapp.data.dao.AthleteDao
import com.activityartapp.data.entities.ActivityEntity
import com.activityartapp.data.entities.AthleteEntity

/**
 * https://svvashishtha.medium.com/using-room-with-hilt-cb57a1bc32f
 * https://developer.android.com/training/data-storage/room
 */

@Database(
    entities = [ActivityEntity::class, AthleteEntity::class],
    version = 5
)
abstract class AthleteDatabase : RoomDatabase() {
    abstract val activityDao: ActivityDao
    abstract val athleteDao: AthleteDao
}