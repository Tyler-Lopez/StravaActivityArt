package com.activityartapp.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.activityartapp.data.entities.ActivityEntity

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
                "WHERE athleteId = :athleteId " +
                "AND summaryPolyline IS NOT NULL"
    )
    suspend fun getActivities(athleteId: Long): List<ActivityEntity>


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
                "WHERE iso8601LocalDate LIKE :yearStringWithDelimiter || '%' " +
                "AND iso8601LocalDate LIKE '%' || :monthStringWithDelimiter || '%' " +
                "AND activityEntity.athleteId = :athleteId "
    )
    suspend fun getActivitiesByYearMonth(
        athleteId: Long,
        monthStringWithDelimiter: String,
        yearStringWithDelimiter: String
    ): List<ActivityEntity>
}