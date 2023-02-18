package com.activityartapp.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.activityartapp.data.entities.AthleteEntity

@Dao
interface AthleteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAthlete(AthleteEntity: AthleteEntity)

    // https://stackoverflow.com/questions/44244508/room-persistance-library-delete-all
    @Query("DELETE FROM athleteEntity")
    suspend fun clearAthlete()

    @Query("SELECT * FROM athleteEntity")
    suspend fun getCurrAthlete(): AthleteEntity?
}