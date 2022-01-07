package com.example.stravaart.network

import com.example.stravaart.model.Athlete
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

// Athlete profile information
// https://developers.strava.com/docs/reference/#api-Athletes-getLoggedInAthlete

interface ApiService {
    @GET("")
    suspend fun getAthlete() : Athlete

    companion object {
        var apiService: ApiService? = null
        fun getInstance() : ApiService {
            if (apiService == null) {
                apiService = Retrofit.Builder()
                    .baseUrl("http://www.strava.com/oauth/authorize?client_id=75992&response_type=code&redirect_uri=http://localhost/exchange_token&approval_prompt=force&scope=read_all")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build().create(ApiService::class.java)
            }
            return apiService!!
        }
    }
}