package com.company.athleteapiart.presentation.filter_month_screen

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.company.athleteapiart.data.entities.ActivityEntity
import com.company.athleteapiart.domain.use_case.ActivitiesUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FilterMonthViewModel @Inject constructor(
    activitiesUseCases: ActivitiesUseCases
) : ViewModel() {

    // Use cases
    private val getActivitiesUseCase = activitiesUseCases.getActivitiesUseCase

    // Data
    private val yearMonthsData = mutableMapOf<Pair<Int, Int>, Int>()

    fun loadActivities(
        context: Context,
        athleteId: Long,
        years: Array<Int>
    ) {
        val unsortedActivities = mutableListOf<Deferred<List<ActivityEntity>>>()
        viewModelScope.launch {
            for (year in years) {
                val yearActivities = async {
                    getActivitiesUseCase.getActivitiesByYearFromCache(
                        context = context,
                        athleteId = athleteId,
                        year = year
                    )
                }
                unsortedActivities.add(yearActivities)
            }
            for (yearlyActivities in unsortedActivities.awaitAll()) {
                for (activity in yearlyActivities) {
                    val key = Pair(activity.activityYear, activity.activityMonth)
                    yearMonthsData[key] =
                        yearMonthsData.getOrDefault(key, 0) + 1
                }
            }
        }

    }


}