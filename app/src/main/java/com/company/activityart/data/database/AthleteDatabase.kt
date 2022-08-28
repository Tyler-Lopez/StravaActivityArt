package com.company.activityart.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.company.activityart.data.Converters
import com.company.activityart.data.dao.AthleteDao
import com.company.activityart.data.entities.AthleteEntity

/**
 * https://svvashishtha.medium.com/using-room-with-hilt-cb57a1bc32f
 * https://developer.android.com/training/data-storage/room
 */

@Database(entities = [AthleteEntity::class], version = 1)
abstract class AthleteDatabase : RoomDatabase() {
    abstract val athleteDao: AthleteDao
}