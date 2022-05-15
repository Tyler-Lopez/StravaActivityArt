package com.company.athleteapiart.presentation.filter_gear_screen

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.company.athleteapiart.data.entities.ActivityEntity
import com.company.athleteapiart.domain.use_case.ActivitiesUseCases
import com.company.athleteapiart.domain.use_case.AthleteUseCases
import com.company.athleteapiart.domain.use_case.GearUseCases
import com.company.athleteapiart.presentation.filter_type_screen.FilterTypeScreenState
import com.company.athleteapiart.presentation.time_select_screen.TimeSelectScreenState
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
    private val getGearsFromRoomUseCase = gearUseCase.getGearsFromRoomUseCase
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
    private val _rows = mutableStateListOf<Map<String, String>>()
    private val columnGear = "GEAR"
    private val columnNoActivities = "NO. ACTIVITIES"
    val rows: List<Map<String, String>> = _rows
    val columns = arrayOf(Pair(columnGear, true), Pair(columnNoActivities, false))

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

            // Flat map all monthly activities within unsorted activities to distinct types
            flatMappedActivities = unsortedActivities.awaitAll().flatMap { monthlyActivities ->
                monthlyActivities.map { activityEntity ->
                    activityEntity
                }.filter {
                    // Filter by activity type if activity type is non-null
                    activityTypes?.contains(it.activityType) ?: true
                }
            }

            // Get athlete and gears from cache
            val athlete = getAthleteUseCase.getAthleteFromRoom(
                context = context,
                athleteId = athleteId
            )
            val gearsCached = athlete?.gears?.toMutableMap() ?: mutableMapOf()


            // Map gears to activity frequency
            val gearFrequencies = flatMappedActivities.groupingBy { it.gearId }.eachCount()

            // Keep track of any new gear caches
            val newCachesDeferred = mutableSetOf<Deferred<Pair<String, String>?>>()

            // Read necessary gearIds from API
            gearFrequencies.filter { !gearsCached.containsKey(it.key) }.forEach {
                // Only execute the following on non-null keys
                it.key?.let { gearId ->
                    println("Debug: Invoked call to Gear API")
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
                                gearsCached[gearId] = gearName
                                gearId to gearName
                            }
                            is Resource.Error -> {
                                null
                            }
                        }
                    }
                    newCachesDeferred.add(cache)
                }
            }

            val newCaches = newCachesDeferred.awaitAll().filterNotNull()

            flatMappedActivities.groupingBy { it.gearId }.eachCount().forEach {
                launch {
                    // If gearID is already cached or is null, do not fetch from API
                    // Read result from API
                    _selected.add(defaultSelected)
                    _rows.add(
                        mapOf(
                            columnGear to (gearsCached[it.key] ?: "Unspecified"),
                            columnNoActivities to "${it.value}"
                        )
                    )
                    recalculateSelected()
                }
            }

            // Update caches
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

}