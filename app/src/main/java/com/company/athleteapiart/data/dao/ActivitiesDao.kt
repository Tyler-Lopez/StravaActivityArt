package com.company.athleteapiart.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.company.athleteapiart.data.remote.responses.Activity

@Dao
interface ActivitiesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActivities(monthYear: String)

    @Query("SELECT * FROM activities WHERE monthYear = :monthYear")
    suspend fun getActivitiesByMonthYear(monthYear: String): List<Activity>
}