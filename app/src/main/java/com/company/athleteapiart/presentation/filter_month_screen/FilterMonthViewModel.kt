package com.company.athleteapiart.presentation.filter_month_screen

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.company.athleteapiart.data.entities.ActivityEntity
import com.company.athleteapiart.domain.use_case.ActivitiesUseCases
import com.company.athleteapiart.presentation.filter_month_screen.FilterMonthScreenState.*
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

    // State - observed in the view
    val filterMonthScreenState = mutableStateOf(LAUNCH)

    // Data - referenced in view
    // (YEAR, MONTH) to (NO. ACTIVITIES)
    private val yearMonthsData = mutableMapOf<Pair<Int, Int>, Int>()

    fun loadActivities(
        context: Context,
        athleteId: Long,
        years: Array<Int>
    ) {
        filterMonthScreenState.value = LOADING

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
            // Iterate through all awaited yearly activities
            for (yearlyActivities in unsortedActivities.awaitAll()) {
                // Iterate through all activities of a given year
                for (activity in yearlyActivities) {
                    // Populate yearsMonthData accordingly
                    val key = Pair(activity.activityYear, activity.activityMonth)
                    yearMonthsData[key] = (yearMonthsData[key] ?: 0) + 1
                }
            }

            filterMonthScreenState.value = STANDBY
        }
    }

    // Invoked to get data in a form for the TableComposable
    fun getColumns() = arrayOf("MONTH", "YEAR", "NO. ACTIVITIES")
    fun getRows(): List<Map<String, Pair<String, Boolean>>> {
        val rows = mutableListOf<Map<String, Pair<String, Boolean>>>()
        for (datum in yearMonthsData) {
            rows.add(
                mapOf(
                    "MONTH" to Pair("${datum.key.second}", true),
                    "YEAR" to Pair("${datum.key.first}", true),
                    "NO. ACTIVITIES" to Pair("${datum.value}", false)
                )
            )
        }
        return rows
    }

}