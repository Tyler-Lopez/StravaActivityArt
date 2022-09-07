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

    /**
     * Retrieves all activities which match the year provided.
     * Functions by pattern recognition of the iso8601 String.
     */
    @Query(
        "SELECT * " +
                "FROM activityEntity " +
                "WHERE iso8601LocalDate LIKE '%' || :year + '-' || '%' " +
                "AND athleteId = :athleteId " +
                "AND summaryPolyline IS NOT NULL"
    )
    suspend fun getActivitiesByYear(
        athleteId: Long,
        year: Int
    ): List<ActivityEntity>


    @Query(
        "SELECT * " +
                "FROM activityEntity " +
                "WHERE iso8601LocalDate LIKE '%' || :year + '-' || '%' " +
                "AND iso8601LocalDate LIKE '%' || '-' + :month + '-' || '%' " +
                "AND activityEntity.athleteId = :athleteId "
    )
    suspend fun getActivitiesByYearMonth(
        athleteId: Long,
        month: String,
        year: Int
    ): List<ActivityEntity>
}