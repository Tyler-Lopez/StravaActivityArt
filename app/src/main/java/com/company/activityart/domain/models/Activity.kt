package com.company.activityart.domain.models

interface Activity {
    val athleteId: Long
    val averageSpeed: Double
    val distance: Double
    val gearId: String?
    val id: Long
    val kudosCount: Int
    val locationCity: String?
    val locationCountry: String?
    val locationState: String?
    val maxSpeed: Double
    /** Zero-indexed month, e.g. January == 0. */
    val month: Int
    val movingTime: Int
    val name: String // "Happy Friday"
    val sufferScore: Int?
    val summaryPolyline: String?
    val type: String // "Ride"
    val year: Int
}