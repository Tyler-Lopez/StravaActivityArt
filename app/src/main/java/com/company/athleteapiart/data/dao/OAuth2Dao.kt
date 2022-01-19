package com.company.athleteapiart.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.company.athleteapiart.data.entities.OAuth2
import com.company.athleteapiart.util.Oauth2

@Dao
interface OAuth2Dao {

    @Insert
    suspend fun insertOauth2(oAuth2: OAuth2)

    @Delete
    suspend fun clearOauth2()

    @Query("SELECT * FROM oauth2")
    suspend fun getOauth2(): Oauth2

}