package com.company.athleteapiart.presentation.filter_type_screen

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.company.athleteapiart.data.entities.ActivityEntity
import com.company.athleteapiart.domain.use_case.ActivitiesUseCases
import com.company.athleteapiart.util.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FilterTypeViewModel @Inject constructor(
    activitiesUseCases: ActivitiesUseCases
) : ViewModel() {

    // Use cases
    private val getActivitiesUseCase = activitiesUseCases.getActivitiesUseCase

    // State - observed in the view
    val filterTypeScreenState = mutableStateOf(FilterTypeScreenState.LAUNCH)

    // Data - referenced in view
    // (YEAR, MONTH) to (NO. ACTIVITIES)
    private val activityTypes = mutableMapOf<String, Int>()
    val selectedTypes = mutableStateListOf<Boolean>()

    val selectedMonthYearsNavArgs: String
        get() = buildString {
            activityTypes.keys.forEachIndexed { index, type ->
                if (selectedTypes[index])
                    append(type).append(Constants.NAV_YEAR_DELIMITER)
            }
        }

    fun loadActivities(
        context: Context,
        athleteId: Long,
        yearMonths: Array<Pair<Int, Int>>
    ) {
        filterTypeScreenState.value = FilterTypeScreenState.LOADING

        val unsortedActivities = mutableListOf<Deferred<List<ActivityEntity>>>()

        viewModelScope.launch {
            for (yearMonth in yearMonths) {
                val monthActivities = async {
                    getActivitiesUseCase.getActivitiesByYearMonthFromCache(
                        context = context,
                        athleteId = athleteId,
                        year = yearMonth.first,
                        month = yearMonth.second
                    )
                }
                unsortedActivities.add(monthActivities)
            }
            // Iterate through all awaited yearly activities
            for (monthActivities in unsortedActivities.awaitAll()) {
                // Iterate through all activities of a given year
                for (activity in monthActivities) {
                    // Record all activity types
                    activityTypes[activity.activityType] =
                        (activityTypes[activity.activityType] ?: 0) + 1
                }
            }
            filterTypeScreenState.value = FilterTypeScreenState.STANDBY
        }
    }

    // Invoked to get data in a form for the TableComposable
    fun getColumns() = arrayOf("TYPE", "NO. ACTIVITIES")
    fun getRows(): List<Map<String, Pair<String, Boolean>>> {
        val rows = mutableListOf<Map<String, Pair<String, Boolean>>>()
        for (datum in activityTypes) {
            rows.add(
                mapOf(
                    "TYPE" to Pair(datum.key, true),
                    "NO. ACTIVITIES" to Pair("${datum.value}", false)
                )
            )
        }
        return rows
    }
}