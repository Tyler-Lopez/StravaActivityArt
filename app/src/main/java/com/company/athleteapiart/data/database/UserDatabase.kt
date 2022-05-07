package com.company.athleteapiart.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.company.athleteapiart.data.dao.UserDao
import com.company.athleteapiart.data.entities.UserEntity


@Database(
    entities = [
        UserEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class UserDatabase : RoomDatabase() {

    abstract val userDao: UserDao

    companion object {
        @Volatile
        // Volatile makes changes visible to all threads
        private var INSTANCE: UserDatabase? = null

        // Application context
        fun getInstance(context: Context): UserDatabase {
            // Synchronized ensures code block only executed by a single thread
            synchronized(this) {
                return INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    UserDatabase::class.java,
                    "userentity_db"
                ).build().also {
                    INSTANCE = it
                }
            }
        }
    }
}