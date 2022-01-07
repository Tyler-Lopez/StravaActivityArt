package com.example.stravaart.network

import retrofit2.http.GET

// Athlete profile information
// https://developers.strava.com/docs/reference/#api-Athletes-getLoggedInAthlete
typealias Athlete = Map<String, String>

interface ApiService {
    @GET("")
    suspend fun getAthlete() : Athlete
}