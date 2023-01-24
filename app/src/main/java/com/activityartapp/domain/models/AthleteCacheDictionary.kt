package com.activityartapp.domain.models

/**
 * Given an athlete's [id], [AthleteCacheDictionary] keeps track of, for each year,
 * the last month successfully cached into ROOM  persistent storage.
 * [lastCachedYearMonth] provides information to make conservative usage of the Strava API.
 *
 * @property id [id] is an athlete's ID, as provided by Strava.
 * @property lastCachedYearMonth [lastCachedYearMonth] is a key-value store.
 * The keys are years, while the values are the last zero-indexed month cached.
 * A year that is fully cached will have a value of 11. A cached year does not necessarily
 * mean that there are any activities cached for this year.
 * Instead, a cached year means that IF any activities exist, they are cached.
 */
interface AthleteCacheDictionary : Athlete {
    override val id: Long
    val lastCachedYearMonth: Map<Int, Int>
}