package com.company.athleteapiart.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.company.athleteapiart.data.dao.AthleteDao
import com.company.athleteapiart.data.entities.AthleteEntity


@Database(
    entities = [
        AthleteEntity::class
    ],
    version = 1,
    exportSchema = false
)

abstract class AthleteDatabase : RoomDatabase() {

    abstract val athleteDao: AthleteDao

    companion object {
        @Volatile
        // Volatile makes changes visible to all threads
        private var INSTANCE: AthleteDatabase? = null

        // Application context
        fun getInstance(context: Context): AthleteDatabase {
            // Synchronized ensures code block only executed by a single thread
            synchronized(this) {
                return INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AthleteDatabase::class.java,
                    "athleteentity_db"
                ).build().also {
                    INSTANCE = it
                }
            }
        }
    }
}