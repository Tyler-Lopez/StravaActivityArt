package com.company.athleteapiart.presentation.filter_month_screen

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
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
    private val _filterMonthScreenState = mutableStateOf(LAUNCH)
    val filterMonthScreenState: State<FilterMonthScreenState> = _filterMonthScreenState

    // Which activities are selected
    private val _selectedActivities = mutableStateListOf<Boolean>()
    private val _selectedActivitiesCount = mutableStateOf(0)
    val selectedActivities: List<Boolean> = _selectedActivities
    val selectedActivitiesCount: State<Int> = _selectedActivitiesCount

    // Rows & Columns
    private val _rows = mutableListOf<Map<String, String>>()
    private val columnMonth = "MONTH"
    private val columnYear = "YEAR"
    private val columnNoActivities = "NO. ACTIVITIES"
    val rows: List<Map<String, String>> = _rows
    val columns =
        arrayOf(Pair(columnMonth, true), Pair(columnYear, true), Pair(columnNoActivities, false))

    // Constants
    private val defaultSelected = true

    // Data - referenced in view
    // (YEAR, MONTH) to (NO. ACTIVITIES)
    //   private val yearMonthsData = mutableStateListOf<Triple<Int, Int, Int>>()
    //   private val gearIds = mutableSetOf<String?>()
    val selectedMonthYearsNavArgs: String
        get() = buildString {
            _rows.forEachIndexed { index, row ->
                if (selectedActivities[index])
                        append(row[columnYear]).append(row[columnMonth])
                        .append(Constants.NAV_YEAR_DELIMITER)
            }
        }


    fun loadActivities(
        context: Context,
        athleteId: Long,
        years: Array<Int>
    ) {
        _filterMonthScreenState.value = LOADING

        viewModelScope.launch {
            val unsortedActivities = mutableListOf<Deferred<List<ActivityEntity>>>()
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
                    println("here month is ${activity.activityMonth}")
                    val key = Pair(activity.activityYear, activity.activityMonth)
                    // Populate yearsMonthData accordingly
                    yearMonthsDataMap[key] = (yearMonthsDataMap[key] ?: 0) + 1
                }
            }
            for (year in years) {
                for (month in 1..12) {
                    if (yearMonthsDataMap.containsKey(Pair(year, month))) {
                        _selectedActivities.add(defaultSelected)
                        _rows.add(
                            mapOf(
                                columnMonth to "$month",
                                columnYear to "$year",
                                columnNoActivities to "${yearMonthsDataMap[Pair(year, month)]}"
                            )
                        )
                        recalculateSelectedActivities()
                    }
                }
            }
            _filterMonthScreenState.value = STANDBY
        }
    }

    fun updateSelectedActivities(index: Int) {
        viewModelScope.launch {
            _selectedActivities[index] = !selectedActivities[index]
            val value = _rows[index][columnNoActivities]?.toInt() ?: 0
            _selectedActivitiesCount.value =
                _selectedActivitiesCount.value + (value * if (_selectedActivities[index]) 1 else -1)
        }
    }

    private fun recalculateSelectedActivities() {
        var sum = 0
        for (index in 0..selectedActivities.lastIndex) {
            val value = _rows[index][columnNoActivities]?.toInt() ?: 0
            sum = selectedActivitiesCount.value + (value * if (selectedActivities[index]) 1 else -1)
        }
        _selectedActivitiesCount.value = sum
    }

}