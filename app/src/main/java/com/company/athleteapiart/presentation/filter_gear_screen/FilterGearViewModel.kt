package com.company.athleteapiart.presentation.filter_gear_screen

import android.content.Context
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.company.athleteapiart.Screen
import com.company.athleteapiart.data.entities.ActivityEntity
import com.company.athleteapiart.domain.use_case.ActivitiesUseCases
import com.company.athleteapiart.domain.use_case.AthleteUseCases
import com.company.athleteapiart.domain.use_case.GearUseCases
import com.company.athleteapiart.presentation.filter_type_screen.FilterTypeScreenState
import com.company.athleteapiart.presentation.time_select_screen.TimeSelectScreenState
import com.company.athleteapiart.util.Constants
import com.company.athleteapiart.util.Resource
import com.company.athleteapiart.util.ScreenState
import com.company.athleteapiart.util.ScreenState.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class FilterGearViewModel @Inject constructor(
    activitiesUseCases: ActivitiesUseCases,
    gearUseCase: GearUseCases,
    athleteUseCases: AthleteUseCases
) : ViewModel() {

    // Use cases
    private val getActivitiesUseCase = activitiesUseCases.getActivitiesUseCase
    private val getAthleteUseCase = athleteUseCases.getAthleteUseCase
    private val getGearFromApiUseCase = gearUseCase.getGearFromApiUseCase
    private val setAthleteUseCase = athleteUseCases.setAthleteUseCase

    // State - observed in the view
    private val _screenState = mutableStateOf(LAUNCH)
    val screenState: State<ScreenState> = _screenState

    // Which activities are selected
    private val _selected = mutableStateListOf<Boolean>()
    private val _selectedCount = mutableStateOf(0)
    val selected: List<Boolean> = _selected
    val selectedCount: State<Int> = _selectedCount

    // Rows & Columns
    private val _rows = mutableStateListOf<Map<String, String?>>()
    private val columnGear = "GEAR"
    private val columnNoActivities = "NO. ACTIVITIES"
    val rows: List<Map<String, String?>> = _rows
    val columns = arrayOf(Pair(columnGear, true), Pair(columnNoActivities, false))

    // Correlate gearIds with gear names
    private lateinit var gearsIdMap: MutableMap<String, String>

    // Constants
    private val defaultSelected = true

    // Keep track of what has occurred
    private lateinit var flatMappedActivities: List<ActivityEntity>

    fun loadActivities(
        context: Context,
        athleteId: Long,
        accessToken: String,
        yearMonths: Array<Pair<Int, Int>>,
        activityTypes: Array<String>? = null
    ) {
        // Set state to loading
        _screenState.value = LOADING

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

            println("here, activity types are $activityTypes")
            // Flat map all monthly activities within unsorted activities to distinct types
            flatMappedActivities = unsortedActivities.awaitAll().flatMap { monthlyActivities ->
                monthlyActivities.map { activityEntity ->
                    activityEntity
                    // Filter by activity type if activity type is non-null
                }.filter { activityTypes?.contains(it.activityType) ?: true }
            }

            // Get athlete and gears from ROOM cache
            val athlete = getAthleteUseCase.getAthleteFromRoom(
                context = context,
                athleteId = athleteId
            )

            gearsIdMap = athlete?.gears?.toMutableMap() ?: mutableMapOf()

            // Map gears to activity frequency
            val gearFrequencies = flatMappedActivities.groupingBy { it.gearId }.eachCount()

            // Keep track of any new gear caches
            val newCachesDeferred = mutableListOf<Deferred<Pair<String, String>?>>()

            // Read necessary gearIds from API
            gearFrequencies.filter { !gearsIdMap.containsKey(it.key) }.forEach {
                // Only execute the following on non-null keys
                it.key?.let { gearId ->
                    // Async load each new gear from API
                    val cache = async {
                        val response = getGearFromApiUseCase.getGearFromApi(
                            gearId = gearId,
                            accessToken = accessToken
                        )
                        when (response) {
                            is Resource.Success -> {
                                val data = response.data
                                val gearName = "${data.brand_name} ${data.model_name}"
                                gearsIdMap[gearId] = gearName
                                gearId to gearName
                            }
                            is Resource.Error -> {
                                null // Todo add some handling in view
                            }
                        }
                    }
                    newCachesDeferred.add(cache)
                }
            }
            val newCaches = newCachesDeferred.awaitAll().filterNotNull()

            // Iterate through Gear Frequency Map and populate rows
            gearFrequencies.forEach {
                launch {
                    _selected.add(defaultSelected)
                    _rows.add(
                        mapOf(
                            columnGear to it.key,
                            columnNoActivities to "${it.value}"
                        )
                    )
                    recalculateSelected()
                }
            }

            // Update Gear ID Correlations in ROOM if necessary
            if (newCaches.isNotEmpty())
                athlete?.let {
                    setAthleteUseCase.setAthlete(
                        context = context,
                        athleteEntity = athlete.withNewGearCaches(newCaches)
                    )
                }
            _screenState.value = STANDBY
        }
    }

    // Invoked in view to say that the user has selected this index
    fun updateSelectedActivities(index: Int) {
        viewModelScope.launch {
            _selected[index] = !_selected[index]
            val value = _rows[index][columnNoActivities]?.toInt() ?: 0
            _selectedCount.value =
                _selectedCount.value + (value * if (_selected[index]) 1 else -1)
        }
    }

    private fun recalculateSelected() {
        var sum = 0
        for (index in 0.._selected.lastIndex) {
            val value = _rows[index][columnNoActivities]?.toInt() ?: 0
            sum = _selectedCount.value + (value * if (_selected[index]) 1 else -1)
        }
        _selectedCount.value = sum
    }


    // For purposes of navigation to next screen
    fun getNavScreen(
        activityTypes: Array<String>? = null
    ): Screen {
        // Filter only those activities which have types and gears which are selected
        val filteredActivities = flatMappedActivities.filter { activity ->

            val selectedGears = _rows.filterIndexed { index, _ ->
                _selected[index]
            }.map { it[columnGear] }

            activity.gearId?.let { selectedGears.contains(activity.activityType) } ?: true &&
                    activityTypes?.contains(activity.activityType) ?: true

        }

        // Within those activities, determine if we need to ask user to filter by gear or distance
        return when {
            // Filter by Distance
            filteredActivities.groupingBy { it.activityDistance }
                .eachCount().size > 1 ->
                Screen.FilterDistance
            // Else, proceed to visualize / format
            else -> Screen.FilterDistance // TODO probably have this direct to the format or visualize screen
        }
    }

    fun yearMonthsToNavArg(yearMonths: Array<Pair<Int, Int>>) = buildString {
        yearMonths.forEach {
            append(it.first).append(it.second)
                .append(Constants.NAV_DELIMITER)
        }
    }

    fun selectedTypesToNavArg(activityTypes: Array<String>? = null) = buildString {
        activityTypes?.forEach {
            append(it).append(Constants.NAV_DELIMITER)
        }
    }

    fun selectedGearsToNavArg() = buildString {
        _rows.forEachIndexed { index, map ->
            if (_selected[index])
                append(map[columnGear]).append(Constants.NAV_DELIMITER)
        }
    }

    // Function converts rows from "gear" to gearId to "gear" to gearName
    fun convertRows() =
        _rows.let { rows ->
            val toReturn = mutableStateListOf<Map<String, String>>()
            rows.forEach { row ->
                toReturn.add(
                    mapOf(
                        columnGear to (gearsIdMap[row[columnGear]] ?: "Unknown"),
                        columnNoActivities to row[columnNoActivities]!!
                    )
                )
            }
            toReturn
        }
}