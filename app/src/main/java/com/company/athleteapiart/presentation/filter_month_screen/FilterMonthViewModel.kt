package com.company.athleteapiart.presentation.filter_month_screen

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.company.athleteapiart.data.entities.ActivityEntity
import com.company.athleteapiart.domain.use_case.ActivitiesUseCases
import com.company.athleteapiart.presentation.filter_month_screen.FilterMonthScreenState.*
import com.company.athleteapiart.util.Constants
import com.company.athleteapiart.util.TimeUtils
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
    private val yearMonthsData = mutableStateListOf<Triple<Int, Int, Int>>()
    val selectedActivities = mutableStateListOf<Boolean>()
    private val activityTypes = mutableSetOf<String>()
    val selectedMonthYearsNavArgs: String
        get() = buildString {
            yearMonthsData.forEachIndexed { index, triple ->
                if (selectedActivities[index])
                    append(triple.first).append(triple.second).append(Constants.NAV_YEAR_DELIMITER)
            }
        }
    val mustFilterActivityType: Boolean
        get() = activityTypes.size > 1

    fun loadActivities(
        context: Context,
        athleteId: Long,
        years: Array<Int>
    ) {
        filterMonthScreenState.value = LOADING

        val unsortedActivities = mutableListOf<Deferred<List<ActivityEntity>>>()

        viewModelScope.launch {
            val yearMonthsDataMap = mutableMapOf<Pair<Int, Int>, Int>()

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
                    // Record all activity types
                    activityTypes.add(activity.activityType)
                    // Populate yearsMonthData accordingly
                    val key = Pair(activity.activityYear, activity.activityMonth)
                    yearMonthsDataMap[key] = (yearMonthsDataMap[key] ?: 0) + 1
                }
            }
            for (year in years) {
                for (month in 1..12) {
                    if (yearMonthsDataMap.containsKey(Pair(year, month))) {
                        yearMonthsData.add(
                            Triple(
                                year,
                                month,
                                yearMonthsDataMap[Pair(year, month)]!!
                            )
                        )
                        selectedActivities.add(true)
                        recalculateSelectedActivities()
                    }
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
                    "MONTH" to Pair(
                        TimeUtils.monthIntToString(datum.second).substring(0, 3).uppercase(), true
                    ),
                    "YEAR" to Pair("${datum.first}", true),
                    "NO. ACTIVITIES" to Pair("${datum.third}", false)
                )
            )
        }
        return rows
    }

    val selectedActivitiesCount = mutableStateOf(0)

    fun updateSelectedActivities(index: Int) {
        viewModelScope.launch {
            selectedActivities[index] = !selectedActivities[index]
            val value = yearMonthsData[index].third
            selectedActivitiesCount.value =
                selectedActivitiesCount.value + (value * if (selectedActivities[index]) 1 else -1)
        }
    }

    private fun recalculateSelectedActivities() {
        var sum = 0
        for (index in 0..selectedActivities.lastIndex) {
            val value = yearMonthsData[index].third
            sum = selectedActivitiesCount.value + (value * if (selectedActivities[index]) 1 else -1)
        }
        selectedActivitiesCount.value = sum
    }

}