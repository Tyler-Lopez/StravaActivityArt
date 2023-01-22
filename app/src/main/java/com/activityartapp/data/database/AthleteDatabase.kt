package com.activityartapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.activityartapp.data.Converters
import com.activityartapp.data.dao.ActivityDao
import com.activityartapp.data.dao.AthleteCacheDictionaryDao
import com.activityartapp.data.dao.OAuth2Dao
import com.activityartapp.data.entities.ActivityEntity
import com.activityartapp.data.entities.AthleteCacheDictionaryEntity
import com.activityartapp.data.entities.OAuth2Entity

/**
 * https://svvashishtha.medium.com/using-room-with-hilt-cb57a1bc32f
 * https://developer.android.com/training/data-storage/room
 */

@Database(
    entities = [ActivityEntity::class, AthleteCacheDictionaryEntity::class, OAuth2Entity::class],
    version = 2
)
@TypeConverters(Converters::class)
abstract class AthleteDatabase : RoomDatabase() {
    abstract val activityDao: ActivityDao
    abstract val athleteCacheDictionaryDao: AthleteCacheDictionaryDao
    abstract val oAuth2Dao: OAuth2Dao
}