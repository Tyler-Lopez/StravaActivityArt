package com.company.athleteapiart.presentation.time_select_screen

import android.content.Context
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

    // In this screen we will store how many activities this user has for each year
    private val loadedActivities = mutableStateListOf<Pair<Int, Int>>()
    val activityCount: Int
        get() = loadedActivities.size

    // State - observed in the view
    val timeSelectScreenState = mutableStateOf(LAUNCH)
    val message: String
        get() = when (timeSelectScreenState.value) {
            LOADING -> "Loading..."
            ERROR -> "Error occurred"
            else -> ""
        }


    // Invoked publicly from Screen in LAUNCH state
    fun loadActivities(
        context: Context,
        athleteId: Long,
        accessToken: String
    ) {
        timeSelectScreenState.value = LOADING

        viewModelScope.launch {

            // Determine if current year and get current month
            val currentYear = Calendar.getInstance().get(Calendar.YEAR)
            val currentMonth = Calendar.getInstance().get(Calendar.MONTH)

            var athlete =
                getAthleteUseCase.getAthlete(
                    context,
                    athleteId,
                    accessToken
                ).data!!

            for (year in Constants.FIRST_YEAR..currentYear) {

                // Get last cached month
                val lastCachedMonth = athlete.lastCachedMonth(year)
                println("HERE FOR $year last cached month was $lastCachedMonth")
                println(athlete.yearMonthsCached)
///*
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

                        if (((currentYear == year) || (currentYear != year && lastCachedMonth != 11)))
                            cacheActivities(
                                context = context,
                                oldAthleteEntity = athlete,
                                activities = data.toTypedArray(),
                                year = year,
                                monthsCached =
                                if (currentYear != year) 11
                                // If it is the current year, lastCachedMonth == lastMonth
                                else currentMonth - 1
                            )
                        athlete = athlete.plusYearsMonthCached(
                            year,
                            if (currentYear != year) 11 else currentMonth - 1
                        )
                        if (data.isNotEmpty()) {
                            println("DATA WAS NOT EMPTY FOR YEAR $year")
                            loadedActivities.add(Pair(year, data.size))
                        } else println("DATA WAS EMPTY FOR YEAR $year")
                    }
                    is Error -> {
                        when (response.fault) {
                            HTTPFault.UNAUTHORIZED -> {

                            }
                            else -> {
                                timeSelectScreenState.value = ERROR
                                return@launch
                            }
                        }

                    }


                }
                // */

            }
            timeSelectScreenState.value = STANDBY
        }
    }

    //   fun getYearlyActivitiesCount(year: Int) = loadedActivities.getOrDefault(year, 0)

    // Update ROOM of ActivityDatabase and AthleteDatabase to cache and reflect cache
    private suspend fun cacheActivities(
        context: Context,
        activities: Array<ActivityEntity>,
        oldAthleteEntity: AthleteEntity,
        year: Int,
        monthsCached: Int
    ) {
        insertActivitiesUseCase.insertActivities(
            context = context,
            activities = activities
        )
        setAthleteUseCases.setAthlete(
            context = context,
            athleteEntity = oldAthleteEntity.plusYearsMonthCached(
                year = year,
                monthsCached = monthsCached
            )
        )
    }

    // Invoked to get data in a form for the TableComposable
    fun getColumns() = arrayOf("YEAR", "NO. ACTIVITIES")
    fun getRows(): List<Map<String, Pair<String, Boolean>>> {
        val rows = mutableListOf<Map<String, Pair<String, Boolean>>>()
        for (datum in loadedActivities) {
            rows.add(
                mapOf(
                    "YEAR" to Pair("${datum.first}", true),
                    "NO. ACTIVITIES" to Pair("${datum.second}", false)
                )
            )
        }
        return rows
    }

    fun selectedActivitiesCount(list: List<Boolean>): Int =
        list.mapIndexed { index, selected ->
            if (selected) loadedActivities[index].second
            else 0
        }.sum()

    fun selectedYearsNavArgs(list: List<Boolean>): String =
        buildString {
            loadedActivities.forEachIndexed { index, pair ->
                if (list[index])
                    append(pair.first).append(Constants.NAV_YEAR_DELIMITER)
            }
        }

}
