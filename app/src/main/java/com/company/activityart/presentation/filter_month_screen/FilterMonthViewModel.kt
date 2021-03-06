package com.company.activityart.presentation.filter_month_screen

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.company.activityart.data.entities.ActivityEntity
import com.company.activityart.domain.use_case.ActivitiesUseCases
import com.company.activityart.presentation.filter_month_screen.FilterMonthScreenState.*
import com.company.activityart.util.NavigationUtils
import com.company.activityart.util.TimeUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
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
    private val _selected = mutableStateListOf<Boolean>()
    private val _selectedCount = mutableStateOf(0)
    val selected: List<Boolean> = _selected
    val selectedCount: State<Int> = _selectedCount

    // Rows & Columns
    private val _rows = mutableListOf<List<Pair<String, Boolean>>>()
    private val columnMonth = "MONTH"
    private val columnYear = "YEAR"
    private val columnNoActivities = "#"
    val rows: List<List<Pair<String, Boolean>>> = _rows
    val columns =
        arrayOf(columnMonth, columnYear, columnNoActivities)

    // Constants
    private val defaultSelected = true


    fun loadActivities(
        context: Context,
        athleteId: Long,
        years: Array<Int>
    ) {
        _filterMonthScreenState.value = LOADING

        viewModelScope.launch(Dispatchers.Default) {
            println("Here years are ${years.joinToString { "$it " }}")
            val unsortedActivities = mutableListOf<List<ActivityEntity>>()
            val yearMonthsDataMap = mutableMapOf<Pair<Int, Int>, Int>()

            for (year in years) {
                val yearActivities =
                    getActivitiesUseCase.getActivitiesByYearFromCache(
                        context = context,
                        athleteId = athleteId,
                        year = year
                    )

                unsortedActivities.add(yearActivities)
            }
            // Iterate through all awaited yearly activities
            for (yearlyActivities in unsortedActivities) {
                // Iterate through all activities of a given year
                for (activity in yearlyActivities) {
                    val key = Pair(activity.activityYear, activity.activityMonth)
                    // Populate yearsMonthData accordingly
                    yearMonthsDataMap[key] = (yearMonthsDataMap[key] ?: 0) + 1
                }
            }
            for (year in years) {
                for (month in 1..12) {
                    if (yearMonthsDataMap.containsKey(Pair(year, month))) {
                        _selected.add(defaultSelected)
                        _rows.add(
                            listOf(
                                TimeUtils.monthIntToString(month) to true,
                                "$year" to true,
                                "${yearMonthsDataMap[Pair(year, month)]}" to false
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
            _selected[index] = !selected[index]
            val value = _rows[index][2].first.toInt() ?: 0
            _selectedCount.value =
                _selectedCount.value + (value * if (_selected[index]) 1 else -1)
        }
    }

    private fun recalculateSelectedActivities() {
        var sum = 0
        for (index in 0..selected.lastIndex) {
            val value = _rows[index][2].first.toInt() ?: 0
            sum = selectedCount.value + (value * if (selected[index]) 1 else -1)
        }
        _selectedCount.value = sum
    }

    // NAVIGATION ARGS
    fun yearMonthsNavArgs() =
        NavigationUtils.yearMonthsNavArgs(_rows.filterIndexed { index, _ -> _selected[index] }
            .map { (it[1].first.toInt()) to (TimeUtils.monthStringToInt(it[0].first)) }
            .toTypedArray())
}