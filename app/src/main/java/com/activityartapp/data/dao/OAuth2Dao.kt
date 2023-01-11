package com.activityartapp.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.activityartapp.data.entities.OAuth2Entity

@Dao
interface OAuth2Dao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOauth2(oAuth2Entity: OAuth2Entity)

    // https://stackoverflow.com/questions/44244508/room-persistance-library-delete-all
    @Query("DELETE FROM oAuth2Entity")
    suspend fun clearOauth2()

    @Query("SELECT * FROM oAuth2Entity")
    suspend fun getCurrAuth(): OAuth2Entity?

}