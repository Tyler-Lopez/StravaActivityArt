package com.company.activityart.presentation.filter_gear_screen

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.company.activityart.util.Screen
import com.company.activityart.data.entities.ActivityEntity
import com.company.activityart.domain.use_case.ActivitiesUseCases
import com.company.activityart.domain.use_case.GearUseCases
import com.company.activityart.util.NavigationUtils
import com.company.activityart.util.Resource
import com.company.activityart.util.ScreenState
import com.company.activityart.util.ScreenState.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class FilterGearViewModel @Inject constructor(
 //   activitiesUseCases: ActivitiesUseCases,
  //  gearUseCase: GearUseCases,
   // athleteUseCases: AthleteUseCases
) : ViewModel() {

    /*
    // Use cases
    private val getActivitiesUseCase = activitiesUseCases.getActivitiesUseCase
    private val getAthleteUseCase = athleteUseCases.getAthleteUseCase
    private val getGearFromApiUseCase = gearUseCase.getGearFromApiUseCase
    private val setAthleteUseCase = athleteUseCases.setAthleteUseCase

    // ViewState - observed in the view
    private val _screenState = mutableStateOf(LAUNCH)
    val screenState: State<ScreenState> = _screenState

    // Which activities are selected
    private val _selected = mutableStateListOf<Boolean>()
    private val _selectedCount = mutableStateOf(0)
    val selected: List<Boolean> = _selected
    val selectedCount: State<Int> = _selectedCount

    // Rows & Columns
    private val _rows = mutableStateListOf<List<Pair<String, Boolean>>>()
    private val columnGear = "GEAR"
    private val columnNoActivities = "#"
    val rows: List<List<Pair<String, Boolean>>> = _rows
    val columns = arrayOf(columnGear, columnNoActivities)

    // Correlate gearIds with gear names
    private lateinit var gearsIdMap: MutableMap<String, String>

    // StringConstants
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
                            is Resource.Failure -> {
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
                println("here in gear frequencies")
                launch {
                    _selected.add(defaultSelected)
                    _rows.add(
                        listOf(
                            (it.key ?: "Unknown") to true,
                            "${it.value}" to false
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
            val value = _rows[index][1].first.toInt()
            _selectedCount.value =
                _selectedCount.value + (value * if (_selected[index]) 1 else -1)
        }
    }

    private fun recalculateSelected() {
        var sum = 0
        for (index in 0.._selected.lastIndex) {
            val value = _rows[index][1].first.toInt()
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
            }.map { it[0].first }

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

    // Function converts rows from "gear" to gearId to "gear" to gearName
    fun convertRows() =
        _rows.let { rows ->
            val toReturn = mutableStateListOf<List<Pair<String, Boolean>>>()
            rows.forEach { row ->
                toReturn.add(
                    listOf(
                        (gearsIdMap[row[0].first] ?: "Unknown") to true,
                        row[1].first to false
                    )
                )
            }
            toReturn
        }

    // NAVIGATION ARGS
    fun gearsNavArgs() = NavigationUtils.gearsNavArgs(
        _rows.filterIndexed { index, _ -> _selected[index] }.map {
            if (it[0].first == "Unknown") "null" else it[0].first
        }.toTypedArray()
    )
    fun activityTypesNavArgs(types: Array<String>?) = NavigationUtils.activityTypesNavArgs(types)
    fun yearMonthsNavArgs(yearMonths: Array<Pair<Int, Int>>) =
        NavigationUtils.yearMonthsNavArgs(yearMonths)

     */
}