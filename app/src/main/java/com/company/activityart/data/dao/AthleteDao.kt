package com.company.activityart.data.dao

import androidx.room.*
import com.company.activityart.data.entities.AthleteEntity

@Dao
interface AthleteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAthlete(athleteEntity: AthleteEntity)

    // https://stackoverflow.com/questions/44244508/room-persistance-library-delete-all
    @Query("DELETE FROM athleteentity")
    suspend fun clearAthlete()

    @Query("SELECT * FROM athleteEntity WHERE athleteEntity.athleteId = :athleteId")
    suspend fun getAthleteById(athleteId: Long): AthleteEntity?

  //  @MapInfo(keyColumn = "gears", valueColumn = "gears")
 //   @Query("SELECT athleteEntity.gears FROM athleteentity WHERE athleteEntity.athleteId = :athleteId")
 //   suspend fun getAthleteGearsById(athleteId: Long): Map<String, String>

}