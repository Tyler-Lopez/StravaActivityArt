package com.company.athleteapiart.presentation.time_select_screen

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.company.athleteapiart.data.entities.ActivityEntity
import com.company.athleteapiart.data.entities.AthleteEntity
import com.company.athleteapiart.domain.use_case.ActivitiesUseCases
import com.company.athleteapiart.domain.use_case.AthleteUseCases
import com.company.athleteapiart.util.Constants
import com.company.athleteapiart.util.Resource.*
import com.company.athleteapiart.presentation.time_select_screen.TimeSelectScreenState.*
import com.company.athleteapiart.util.HTTPFault
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class TimeSelectViewModel @Inject constructor(
    athleteUseCases: AthleteUseCases,
    activitiesUseCases: ActivitiesUseCases
) : ViewModel() {

    // Use cases
    private val getActivitiesUseCase = activitiesUseCases.getActivitiesUseCase
    private val insertActivitiesUseCase = activitiesUseCases.insertActivitiesUseCase
    private val getAthleteUseCase = athleteUseCases.getAthleteUseCase
    private val setAthleteUseCases = athleteUseCases.setAthleteUseCase

    // Which activities are selected
    private val _selectedActivities = mutableStateListOf<Boolean>()
    private val _selectedActivitiesCount = mutableStateOf(0)
    val selectedActivities: List<Boolean> = _selectedActivities
    val selectedActivitiesCount: State<Int> = _selectedActivitiesCount

    // Rows & Columns
    private val _rows = mutableStateListOf<Map<String, String>>()
    private val columnYear = "YEAR"
    private val columnNoActivities = "NO. ACTIVITIES"
    val rows: List<Map<String, String>> = _rows
    val columns = arrayOf(Pair(columnYear, true), Pair(columnNoActivities, false))

    // Screen State
    private val _timeSelectScreenState = mutableStateOf(LAUNCH)
    val timeSelectScreenState: State<TimeSelectScreenState> = _timeSelectScreenState

    // Constants
    private val defaultSelected = false

    // Invoked upon LAUNCH
    fun loadActivities(
        context: Context,
        athleteId: Long,
        accessToken: String
    ) {
        // Set state to loading
        _timeSelectScreenState.value = LOADING

        viewModelScope.launch {

            // Clear loaded activities - probably would be improved if we just
            // added some logic to keep old data but specifically not repeat it
            _rows.clear()

            // Determine if current year and get current month
            val currentYear = Calendar.getInstance().get(Calendar.YEAR)
            val currentMonth = Calendar.getInstance().get(Calendar.MONTH)

            // Get athlete - important for knowing IF activities have been cached
            var athlete = getAthleteUseCase.getAthlete(
                context,
                athleteId,
                accessToken
            ).data!!

            val newCaches = mutableMapOf<Int, Int>()
            launch {
                // Iterate through all years
                for (year in 2021..currentYear) {
                    launch {
                        // Get last cached month of this year
                        val lastCachedMonth = athlete.lastCachedMonth(year)
                        // Get activities from either ROOM and/or the Strava API
                        val response = getActivitiesUseCase
                            .getActivitiesByYear(
                                context = context,
                                athleteEntity = athlete,
                                accessToken = accessToken,
                                year = year
                            )

                        when (response) {
                            is Success -> {
                                val data = response.data
                                // Determine if we need to cacheActivities, and if so, perform cache
                                // If NOT the current year and lastCachedMonth of that year != 11, cache
                                // If current year, always cache whole year but set lastCachedMonth to currentMonth - 1
                                //      - This means all future calls will always check API for most recent month.

                                if ((currentYear == year || (currentYear != year && lastCachedMonth != 12))) {
                                    newCaches[year] = if (currentYear != year)
                                        12
                                    else currentMonth

                                    cacheActivities(
                                        context = context,
                                        activities = data.toTypedArray()
                                    )
                                }

                                println("data is $data")
                                // The athlete has recorded some activities for this year
                                if (data.isNotEmpty()) {
                                    println(data[data.lastIndex].activityMonth)
                                    _selectedActivities.add(defaultSelected)
                                    _rows.add(
                                        mapOf(
                                            columnYear to "$year",
                                            columnNoActivities to "${data.size}"
                                        )
                                    )
                                }
                            }
                            is Error -> {
                                // TODO
                                // Provide better information to view upon Error
                                when (response.fault) {
                                    HTTPFault.UNAUTHORIZED -> {

                                    }
                                    else -> {

                                    }

                                }
                                _timeSelectScreenState.value = ERROR
                            }
                        }
                    }
                }
            }.invokeOnCompletion {
                viewModelScope.launch {
                    // Todo boy this is a mess, clean this up later - the point is just to update cache with newCaches
                    println("now trying to set")
                    println(athlete.yearMonthsCached)
                    println("now trying to set")
                    println(athlete.yearMonthsCached)
                    val newMap = mutableMapOf<Int, Int>()
                    athlete.yearMonthsCached.forEach {
                        newMap.put(it.key, it.value)
                    }
                    newCaches.forEach { i, i2 ->
                        newMap.put(i, i2)
                    }
                    setAthleteUseCases.setAthlete(
                        context = context,
                        athleteEntity = athlete.withNewCache(newMap)
                    )
                    println("finished setting")
                }
            }
        }
    }

    // Update ROOM of ActivityDatabase and AthleteDatabase to cache and reflect cache
    private suspend fun cacheActivities(
        context: Context,
        activities: Array<ActivityEntity>
    ) {
        insertActivitiesUseCase.insertActivities(
            context = context,
            activities = activities
        )
    }

    // Invoked in view to say that the user has selected this index
    fun updateSelectedActivities(index: Int) {
        viewModelScope.launch {
            _selectedActivities[index] = !selectedActivities[index]
            val value = _rows[index][columnNoActivities]?.toInt() ?: 0
            _selectedActivitiesCount.value =
                _selectedActivitiesCount.value + (value * if (selectedActivities[index]) 1 else -1)
        }
    }

    fun selectedYearsNavArgs(): String =
        buildString {
            _rows.forEachIndexed { index, pair ->
                if (selectedActivities[index])
                    append(pair[columnYear]).append(Constants.NAV_YEAR_DELIMITER)
            }
        }

}
