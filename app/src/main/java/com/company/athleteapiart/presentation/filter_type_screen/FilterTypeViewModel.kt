package com.company.athleteapiart.presentation.filter_type_screen

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.company.athleteapiart.Screen
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
    private val _filterTypeScreenState = mutableStateOf(FilterTypeScreenState.LAUNCH)
    val filterTypeScreenState: State<FilterTypeScreenState> = _filterTypeScreenState

    // Which activities are selected
    private val _selectedTypes = mutableStateListOf<Boolean>()
    private val _selectedTypesCount = mutableStateOf(0)
    val selectedTypes: List<Boolean> = _selectedTypes
    val selectedTypesCount: State<Int> = _selectedTypesCount


    // Rows & Columns
    private val _rows = mutableStateListOf<Map<String, String>>()
    private val columnType = "TYPE"
    private val columnNoActivities = "NO. ACTIVITIES"
    val rows: List<Map<String, String>> = _rows
    val columns = arrayOf(Pair(columnType, true), Pair(columnNoActivities, false))

    // Constants
    private val defaultSelected = true

    // Keep track of what has occurred
    private lateinit var flatMappedActivities: List<ActivityEntity>

    fun loadActivities(
        context: Context,
        athleteId: Long,
        yearMonths: Array<Pair<Int, Int>>
    ) {
        // Set state to loading
        _filterTypeScreenState.value = FilterTypeScreenState.LOADING

        viewModelScope.launch {

            // Make async calls to each month that we should load then await
            val unsortedActivities = mutableListOf<Deferred<List<ActivityEntity>>>()

            for (yearMonth in yearMonths) {
                unsortedActivities.add(async {
                    getActivitiesUseCase.getActivitiesByYearMonthFromCache(
                        context = context,
                        athleteId = athleteId,
                        year = yearMonth.first,
                        month = yearMonth.second
                    )
                })
            }

            // Flat map all monthly activities within unsorted activities to distinct types
            // Then group into a map like { Walk = 100 } and add to rows
            flatMappedActivities = unsortedActivities.awaitAll().flatMap { monthlyActivities ->
                monthlyActivities.map { activityEntity ->
                    activityEntity
                }
            }
            // Sum activity types
            flatMappedActivities.groupingBy { it.activityType }.eachCount().forEach {
                _selectedTypes.add(defaultSelected)
                _rows.add(
                    mapOf(
                        columnType to it.key,
                        columnNoActivities to "${it.value}"
                    )
                )
                recalculateSelected()
            }
            _filterTypeScreenState.value = FilterTypeScreenState.STANDBY
        }
    }

    // Invoked in view to say that the user has selected this index
    fun updateSelectedActivities(index: Int) {
        viewModelScope.launch {
            _selectedTypes[index] = !_selectedTypes[index]
            val value = _rows[index][columnNoActivities]?.toInt() ?: 0
            _selectedTypesCount.value =
                _selectedTypesCount.value + (value * if (_selectedTypes[index]) 1 else -1)
        }
    }

    private fun recalculateSelected() {
        var sum = 0
        for (index in 0.._selectedTypes.lastIndex) {
            val value = _rows[index][columnNoActivities]?.toInt() ?: 0
            sum = _selectedTypesCount.value + (value * if (_selectedTypes[index]) 1 else -1)
        }
        _selectedTypesCount.value = sum
    }

    // For purposes of navigation to next screen
    fun getNavScreen(): Screen {
        // Filter only those activities which have types which are selected
        val filteredActivities = flatMappedActivities.filter { activity ->
            _rows.filterIndexed { index, _ ->
                _selectedTypes[index]
            }.map { it[columnType] }.contains(activity.activityType)
        }

        // Within those activities, determine if we need to ask user to filter by gear or distance
        return when {
            // Filter by Gear
            filteredActivities.groupingBy { it.gearId }.eachCount().size > 1 ->
                Screen.FilterGear
            // Filter by Distance
            filteredActivities.groupingBy { it.activityDistance }
                .eachCount().size > 1 ->
                Screen.FilterDistance
            // Else, proceed to visualize / format
            else -> Screen.FilterDistance // TODO probably have this direct to the format or visualize screen
        }
    }

    fun yearMonthsToNavArg(yearMonths: Array<Pair<Int, Int>>) = buildString {
        println("here $yearMonths yearmonthss")
        yearMonths.forEach {
            append(it.first).append(it.second)
                .append(Constants.NAV_DELIMITER)
        }
    }

    fun selectedTypesToNavArg() = buildString {
        _rows.forEachIndexed { index, map ->
            if (_selectedTypes[index])
                append(map[columnType]).append(Constants.NAV_DELIMITER)
        }
    }
}