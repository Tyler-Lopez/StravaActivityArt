package com.activityartapp.data.dao

import androidx.room.*
import com.activityartapp.data.entities.AthleteCacheDictionaryEntity

@Dao
interface AthleteCacheDictionaryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAthlete(athleteEntity: AthleteCacheDictionaryEntity)

    // https://stackoverflow.com/questions/44244508/room-persistance-library-delete-all
    @Query("DELETE FROM athleteCacheDictionaryEntity")
    suspend fun clearAthleteCacheDictionary()

    @Query("SELECT * FROM athleteCacheDictionaryEntity WHERE athleteCacheDictionaryEntity.id = :athleteId")
    suspend fun getAthleteCacheDictionaryById(athleteId: Long): AthleteCacheDictionaryEntity?
}