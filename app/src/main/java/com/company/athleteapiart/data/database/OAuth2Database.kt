package com.company.athleteapiart.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.company.athleteapiart.data.dao.OAuth2Dao
import com.company.athleteapiart.data.entities.OAuth2

@Database(
    entities = [
        OAuth2::class
    ],
    version = 1
)
abstract class OAuth2Database : RoomDatabase() {

    abstract val oAuth2Dao: OAuth2Dao

    companion object {
        @Volatile
        // Volatile makes changes visible to all threads
        private var INSTANCE: OAuth2Database? = null

        // Application context
        fun getInstance(context: Context): OAuth2Database {
            // Synchronized ensures code block only executed by a single thread
            synchronized(this) {
                return INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    OAuth2Database::class.java,
                    "oauth2_db"
                ).build().also {
                    INSTANCE = it
                }
            }
        }
    }
}