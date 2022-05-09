package com.company.athleteapiart.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.company.athleteapiart.data.entities.AthleteEntity

@Dao
interface AthleteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAthlete(athleteEntity: AthleteEntity)

    // https://stackoverflow.com/questions/44244508/room-persistance-library-delete-all
    @Query("DELETE FROM athleteentity")
    suspend fun clearAthlete()

    @Query("SELECT * FROM athleteEntity WHERE athleteEntity.athleteId = :athleteId")
    suspend fun getAthleteById(athleteId: Long): AthleteEntity?

}