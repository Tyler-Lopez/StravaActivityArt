package com.activityartapp.domain.models

import com.activityartapp.util.enums.SportType

interface Activity {
    val athleteId: Long
    val averageSpeed: Double
    val distance: Double
    val gearId: String?
    val id: Long
    val iso8601LocalDate: String
    val kudosCount: Int
    val locationCity: String?
    val locationCountry: String?
    val locationState: String?
    val maxSpeed: Double
    val movingTime: Int
    val name: String // "Happy Friday"
    val sufferScore: Int?
    val summaryPolyline: String?
    val sportType: SportType

}