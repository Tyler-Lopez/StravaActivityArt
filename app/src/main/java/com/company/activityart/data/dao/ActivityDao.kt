package com.company.activityart.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.company.activityart.data.entities.ActivityEntity

@Dao
interface ActivityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllActivities(vararg activityEntity: ActivityEntity)

    @Query("SELECT * " +
            "FROM activityentity " +
            "WHERE activityEntity.activityYear = :year " +
            "AND activityEntity.athleteId = :athleteId " +
            "AND activityEntity.summaryPolyline IS NOT NULL")
    suspend fun getActivitiesByYear(
        athleteId: Long,
        year: Int
    ): List<ActivityEntity>

    @Query("SELECT * FROM activityentity WHERE activityEntity.activityYear = :year AND activityEntity.activityMonth = :month AND activityEntity.athleteId = :athleteId AND activityEntity.summaryPolyline IS NOT NULL")
    suspend fun getActivitiesByYearMonth(
        athleteId: Long,
        month: Int,
        year: Int
    ): List<ActivityEntity>

    @Query(
        "SELECT * " +
                "FROM activityentity " +
                "WHERE activityEntity.activityYear = :year " +
                "AND activityEntity.activityMonth <= :month " +
                "AND activityEntity.athleteId = :athleteId " +
                "AND activityEntity.summaryPolyline IS NOT NULL"
    )
    suspend fun getActivitiesByYearUpToMonth(
        athleteId: Long,
        month: Int,
        year: Int
    ): List<ActivityEntity>

}