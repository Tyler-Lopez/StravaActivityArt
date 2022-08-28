package com.company.activityart.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.company.activityart.data.entities.OAuth2Entity

@Dao
interface OAuth2Dao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOauth2(oAuth2Entity: OAuth2Entity)

    // https://stackoverflow.com/questions/44244508/room-persistance-library-delete-all
    @Query("DELETE FROM oauth2entity")
    suspend fun clearOauth2()

    @Query("SELECT * FROM oauth2entity WHERE oauth2entity.athleteId = :athleteId")
    suspend fun getOauth2ByAthleteId(athleteId: Long): OAuth2Entity?

}