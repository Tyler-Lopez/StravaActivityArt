package com.activityartapp.domain.models

import java.util.concurrent.TimeUnit


/**
 * Interface abstraction based on the following StackOverflow response.
 * https://stackoverflow.com/questions/53508484/clean-architecture-share-same-models-entities-with-different-layers
 */
interface Athlete {
    val athleteId: Long
    // TODO COMMENTED FOR NOW val userName: String?
    // TODO COMMENTED FOR NOW val receivedOnUnixSeconds: Int?
    // TODO COMMENTED FOR NOW val profilePictureMedium: String
    // TODO COMMENTED FOR NOW val profilePictureLarge: String
    // TODO COMMENTED FOR NOW val firstName: String
    // TODO COMMENTED FOR NOW val lastName: String
    // This is to tell us what we have and have not yet mapped to Room
    /*
    val yearMonthsCached: Map<Int, Int>
    val gears: Map<String, String>

     */
    val lastCachedYearMonth: Map<Int, Int>
}