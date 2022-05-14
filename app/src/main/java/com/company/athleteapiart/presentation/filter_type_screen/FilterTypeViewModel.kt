package com.company.athleteapiart.presentation.filter_type_screen

import android.content.Context
import androidx.compose.runtime.State
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
import kotlin.properties.Delegates

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
    private var filterByGear = false
    private var filterByDistance = false
    private lateinit var flatMappedActivities: List<ActivityEntity>

    fun getNavArgs(): String {
        // Filter only those activities which have types which are selected
        val filteredActivities = flatMappedActivities.filter { activity ->
            rows.map { it[columnType] }.contains(activity.activityType)
        }
        filterByGear = filteredActivities.groupingBy { it.gearId }.eachCount().size > 1
        filterByDistance =
            filteredActivities.groupingBy { it.activityDistance }.eachCount().size > 1
        
        return when {
            filterByGear -> ""
            filterByDistance -> ""
            else -> ""
        }
    }

    // val selectedMonthYearsNavArgs: String
    ///   get() = buildString {
    //     activityTypes.keys.forEachIndexed { index, type ->
    //          if (selectedTypes[index])
    //               append(type).append(Constants.NAV_YEAR_DELIMITER)
    //        }
    //     }
    // Determine navigation arguments


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
            _selectedTypes[index] = !selectedTypes[index]
            val value = _rows[index][columnNoActivities]?.toInt() ?: 0
            _selectedTypesCount.value =
                _selectedTypesCount.value + (value * if (selectedTypes[index]) 1 else -1)
        }
    }

    private fun recalculateSelected() {
        var sum = 0
        for (index in 0..selectedTypes.lastIndex) {
            val value = _rows[index][columnNoActivities]?.toInt() ?: 0
            sum = selectedTypesCount.value + (value * if (selectedTypes[index]) 1 else -1)
        }
        _selectedTypesCount.value = sum
    }
}