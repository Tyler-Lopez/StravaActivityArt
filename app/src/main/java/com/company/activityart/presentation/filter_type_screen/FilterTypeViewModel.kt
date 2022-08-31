package com.company.activityart.presentation.filter_type_screen

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.company.activityart.util.Screen
import com.company.activityart.data.entities.ActivityEntity
import com.company.activityart.domain.use_case.ActivitiesUseCases
import com.company.activityart.util.NavigationUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FilterTypeViewModel @Inject constructor(
    //activitiesUseCases: ActivitiesUseCases
) : ViewModel() {

    /*
    // Use cases
    private val getActivitiesUseCase = activitiesUseCases.getActivitiesUseCase

    // ViewState - observed in the view
    private val _filterTypeScreenState = mutableStateOf(FilterTypeScreenState.LAUNCH)
    val filterTypeScreenState: State<FilterTypeScreenState> = _filterTypeScreenState

    // Which activities are selected
    private val _selectedTypes = mutableStateListOf<Boolean>()
    private val _selectedTypesCount = mutableStateOf(0)
    val selectedTypes: List<Boolean> = _selectedTypes
    val selectedTypesCount: State<Int> = _selectedTypesCount


    // Rows & Columns
    private val _rows = mutableStateListOf<List<Pair<String, Boolean>>>()
    private val columnType = "TYPE"
    private val columnNoActivities = "#"
    val rows: List<List<Pair<String, Boolean>>> = _rows
    val columns = arrayOf(columnType, columnNoActivities)

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
                    listOf(
                        it.key to true,
                        "${it.value}" to false
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
            val value = _rows[index][1].first.toInt()
            _selectedTypesCount.value =
                _selectedTypesCount.value + (value * if (_selectedTypes[index]) 1 else -1)
        }
    }

    private fun recalculateSelected() {
        var sum = 0
        for (index in 0.._selectedTypes.lastIndex) {
            val value = _rows[index][1].first.toInt()
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
            }.map { it[0].first }.contains(activity.activityType)
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

    // NAVIGATION ARGS
    fun activityTypesNavArgs() = NavigationUtils.activityTypesNavArgs(
        _rows.filterIndexed { index, _ -> _selectedTypes[index] }
            .map { it[0].first }
            .toTypedArray()
    )
    fun yearMonthsNavArgs(yearMonths: Array<Pair<Int, Int>>) =
        NavigationUtils.yearMonthsNavArgs(yearMonths)

     */
}