package com.company.athleteapiart.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.company.athleteapiart.data.dao.ActivityDao
import com.company.athleteapiart.data.entities.ActivityEntity


@Database(
    entities = [
        ActivityEntity::class
    ],
    version = 1,
    exportSchema = false
)

abstract class ActivityDatabase : RoomDatabase() {

    abstract val activityDao: ActivityDao

    companion object {
        @Volatile
        // Volatile makes changes visible to all threads
        private var INSTANCE: ActivityDatabase? = null

        // Application context
        fun getInstance(context: Context): ActivityDatabase {
            // Synchronized ensures code block only executed by a single thread
            synchronized(this) {
                return INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    ActivityDatabase::class.java,
                    "activityentity_db"
                ).build().also {
                    INSTANCE = it
                }
            }
        }
    }
}