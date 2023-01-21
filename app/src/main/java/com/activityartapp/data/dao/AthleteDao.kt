package com.activityartapp.data.dao

import androidx.room.*
import com.activityartapp.data.entities.AthleteEntity

@Dao
interface AthleteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAthlete(athleteEntity: AthleteEntity)

    // https://stackoverflow.com/questions/44244508/room-persistance-library-delete-all
    @Query("DELETE FROM athleteEntity")
    suspend fun clearAthlete()

    @Query("SELECT * FROM athleteEntity WHERE athleteEntity.id = :athleteId")
    suspend fun getAthleteById(athleteId: Long): AthleteEntity?

}