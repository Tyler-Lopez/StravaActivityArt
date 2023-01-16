package com.activityartapp.domain.use_case.activities

import com.activityartapp.data.database.AthleteDatabase
import com.activityartapp.domain.models.Activity
import com.activityartapp.domain.models.Athlete
import javax.inject.Inject

/**
 * Retrieves all [Activity] stored locally associated with an
 * [Athlete] occurring on the given year and month.
 */
class GetActivitiesByYearMonthFromLocalUseCase @Inject constructor(
    private val athleteDatabase: AthleteDatabase
) {
    companion object {
        private const val MONTH_QUERY_MIN_LENGTH = 2
        private const val PADDING_CHAR: Char = '0'
        private const val DELIMITER_CHAR = '-'
    }

    /**
     * @param month A 0-indexed value representing the month ranging from 0 to 11.
     */
    suspend operator fun invoke(athleteId: Long, month: Int, year: Int): List<Activity> {
        /** ISO8601 is NOT 0-indexed with respect to month and thus must be reversed.
         * Month is a padded integer which ranges from 01 to 12. **/
        val paddedMonth = "${(month + 1)}".padStart(
            MONTH_QUERY_MIN_LENGTH,
            PADDING_CHAR
        )
        return athleteDatabase
            .activityDao
            .getActivitiesByYearMonth(
                athleteId = athleteId,
                monthStringWithDelimiter = "$DELIMITER_CHAR$paddedMonth$DELIMITER_CHAR",
                yearStringWithDelimiter = "$year$DELIMITER_CHAR"
            )
    }
}