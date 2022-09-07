package com.company.activityart.domain.use_case.activities

import com.company.activityart.data.database.AthleteDatabase
import com.company.activityart.domain.models.Activity
import javax.inject.Inject

class GetActivitiesByYearMonthFromLocalUseCase @Inject constructor(
    private val athleteDatabase: AthleteDatabase
) {
    companion object {
        private const val MONTH_QUERY_MIN_LENGTH = 2
        private const val PADDING_DIGIT = 0
    }

    suspend operator fun invoke(athleteId: Long, month: Int, year: Int): List<Activity> {
        return athleteDatabase
            .activityDao
            .getActivitiesByYearMonth(
                athleteId,
                "$month".padStart(
                    MONTH_QUERY_MIN_LENGTH,
                    PADDING_DIGIT.toChar()
                ),
                year
            )
    }
}