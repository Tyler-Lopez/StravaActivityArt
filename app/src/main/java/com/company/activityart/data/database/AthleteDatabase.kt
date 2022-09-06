package com.company.activityart.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.company.activityart.data.Converters
import com.company.activityart.data.dao.ActivityDao
import com.company.activityart.data.dao.AthleteDao
import com.company.activityart.data.dao.OAuth2Dao
import com.company.activityart.data.entities.ActivityEntity
import com.company.activityart.data.entities.AthleteEntity
import com.company.activityart.data.entities.OAuth2Entity

/**
 * https://svvashishtha.medium.com/using-room-with-hilt-cb57a1bc32f
 * https://developer.android.com/training/data-storage/room
 */

@Database(
    entities = [ActivityEntity::class, AthleteEntity::class, OAuth2Entity::class],
    version = 2
)
abstract class AthleteDatabase : RoomDatabase() {
    abstract val activityDao: ActivityDao
    abstract val athleteDao: AthleteDao
    abstract val oAuth2Dao: OAuth2Dao
}