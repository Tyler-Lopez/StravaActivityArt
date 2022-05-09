package com.company.athleteapiart.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.company.athleteapiart.data.entities.ActivityEntity

@Dao
interface ActivityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllActivities(vararg activityEntity: ActivityEntity)

    @Query("SELECT * FROM activityentity WHERE activityEntity.activityYear = :year AND activityEntity.athleteId = :athleteId")
    suspend fun getActivitiesByYear(
        athleteId: Int,
        year: Int
    ): List<ActivityEntity>

    @Query("SELECT * FROM activityentity WHERE activityEntity.activityYear = :year AND activityEntity.activityMonth = :month AND activityEntity.athleteId = :athleteId")
    suspend fun getActivitiesByMonthYear(
        athleteId: Int,
        month: Int,
        year: Int
    ): List<ActivityEntity>

}