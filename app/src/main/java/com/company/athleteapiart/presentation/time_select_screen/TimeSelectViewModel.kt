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

    // TEMP TO DEBUG
    val loadedActivities = mutableStateOf<List<ActivityEntity>?>(null)

    fun loadActivities(
        context: Context,
        athleteId: Long,
        accessToken: String
    ) {
        timeSelectScreenState.value = TimeSelectScreenState.LOADING

        viewModelScope.launch {

            val athlete = athleteUseCases.getAthleteUseCase.getAthlete(context, athleteId, accessToken).data!!
            val yearsMonthsCached: MutableMap<Int, Int> = mutableMapOf()
            yearsMonthsCached.putAll(athlete.yearMonthsCached)

            // Determine if current year and get current month
            val currentYear = Calendar.getInstance().get(Calendar.YEAR)

            val year = 2018
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

            val data = response.data

            if (currentYear != year)
                yearsMonthsCached[year] = 11

            insertActivitiesUseCase.insertActivities(
                context = context,
                activities = response.data?.toTypedArray() ?: arrayOf()
            )
            println("Here $yearsMonthsCached")
            println("Here ${yearsMonthsCached[2018]}")
            setAthleteUseCases.setAthlete(
                context = context,
                athleteEntity = AthleteEntity(
                    athleteId = athlete.athleteId,
                    userName = athlete.userName,
                    receivedOn = athlete.receivedOn,
                    profilePictureMedium = athlete.profilePictureMedium,
                    profilePictureLarge =  athlete.profilePictureLarge,
                    firstName = athlete.firstName,
                    lastName = athlete.lastName,
                    yearMonthsCached = yearsMonthsCached
                )
            )



            when (response) {
                is Success -> {

                }
                is Error -> {

                }
            }


            loadedActivities.value = response.data
            println(response.message + " is response")


            timeSelectScreenState.value = TimeSelectScreenState.STANDBY
        }
    }
    /*
    var loadError = mutableStateOf("")
    var isLoading = mutableStateOf(false)
    var endReached = mutableStateOf(false)
    var activities = mutableStateListOf<Activity>()

    fun loadActivitiesByYear(year: Int) {
        // Ensure we don't constantly invoke this
        // May be a better way to do this?
        // Overly cautious to not recursively invoke api right now
        if (isLoading.value || endReached.value) return
        isLoading.value = true


        viewModelScope.launch {
            AthleteActivities.currYear.value = year
            val after = (GregorianCalendar(year, 0, 1).timeInMillis / 1000).toInt()
            val beforeDate = GregorianCalendar(year + 1, 0, 1)
            beforeDate.add(GregorianCalendar.DAY_OF_MONTH, -1)
            val before = (beforeDate.timeInMillis / 1000).toInt()

            AthleteActivities.activities.value.clear()
            getActivities(
                page = 1,
                before = before,
                after = after
            )
        }
    }

    private fun getActivities(page: Int, before: Int, after: Int) {
/*
  viewModelScope.launch {

       when (val result = repository
           .getActivities(
               page = page,
               perPage = 100,
               before = before,
               after = after
           )
       ) {
           is Resource.Success -> {
               if (result.data.size < 100) {
                   for (activity in result.data) {
                       if (activity.map.summary_polyline != "null" && activity.map.summary_polyline != null)
                           activities.add(activity)
                   }
                   AthleteActivities.activities.value = activities
                   endReached.value = true
                   isLoading.value = false
               } else {
                   for (activity in result.data) {
                       if (activity.map.summary_polyline != "null" && activity.map.summary_polyline != null)
                           activities.add(activity)
                   }
                  // activities.addAll(result.data)
                   getActivities(page + 1, before, after)
               }
           }
           is Resource.Error -> {
               if (result.message.contains("timeout", true)) {
                   println("timed out trying again")
                   getActivities(page, before, after)
               } else if (result.message.contains("Unable to resolve host")) {
                   println("unable to resolve host trying again")
                   getActivities(page, before, after)
               } else {
                   //   getActivities(page, before, after)
                   println("error here")
                   loadError.value = result.message
                   isLoading.value = false
                   endReached.value = true
               }
           }
       }
        */


}
     */
}
