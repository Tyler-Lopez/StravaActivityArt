package com.company.athleteapiart.presentation.time_select_screen

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.company.athleteapiart.data.entities.ActivityEntity
import com.company.athleteapiart.data.entities.AthleteEntity
import com.company.athleteapiart.domain.use_case.ActivitiesUseCases
import com.company.athleteapiart.domain.use_case.AthleteUseCases
import com.company.athleteapiart.util.Resource.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class TimeSelectViewModel @Inject constructor(
    private val athleteUseCases: AthleteUseCases,
    private val activitiesUseCases: ActivitiesUseCases
) : ViewModel() {

    // Use cases
    private val getActivitiesUseCase = activitiesUseCases.getActivitiesUseCase
    private val insertActivitiesUseCase = activitiesUseCases.insertActivitiesUseCase
    private val getAthleteUseCases = athleteUseCases.getAthleteUseCase
    private val setAthleteUseCases = athleteUseCases.setAthleteUseCase

    // State - observed in the view
    val timeSelectScreenState = mutableStateOf(TimeSelectScreenState.LAUNCH)

    // In this screen we will store how many activities this user has for each year
    val loadedActivities = mutableStateOf<Map<Int, Int>?>(null)

    // Invoked publicly from Screen in LAUNCH state
    fun loadActivities(
        context: Context,
        athleteId: Long,
        accessToken: String
    ) {
        timeSelectScreenState.value = TimeSelectScreenState.LOADING

        viewModelScope.launch {

            val athlete =
                athleteUseCases.getAthleteUseCase.getAthlete(context, athleteId, accessToken).data!!


            val year = 2018

            // Determine if current year and get current month
            val currentYear = Calendar.getInstance().get(Calendar.YEAR)
            val currentMonth = Calendar.getInstance().get(Calendar.MONTH)
            val lastCachedMonth = athlete.lastCachedMonth(year)

            // Get activities from either ROOM and/or the Strava API
            val response = getActivitiesUseCase
                .getActivitiesByYear(
                    context = context,
                    athleteEntity = getAthleteUseCases.getAthlete(
                        context,
                        athleteId,
                        accessToken
                    ).data!!,
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
                    if ((currentYear == year) || (currentYear != year && lastCachedMonth != 11))
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

                }
                is Error -> {

                }
            }

            println(response.message + " is response")
            timeSelectScreenState.value = TimeSelectScreenState.STANDBY

        }
    }

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
}
