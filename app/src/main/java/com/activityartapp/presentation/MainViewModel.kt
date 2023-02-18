package com.activityartapp.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.activityartapp.architecture.BaseRoutingViewModel
import com.activityartapp.data.cache.ActivitiesCache
import com.activityartapp.domain.useCase.activities.InsertActivitiesIntoMemory
import com.activityartapp.domain.useCase.authentication.GetAccessTokenFromDiskOrRemote
import com.activityartapp.presentation.MainViewState.*
import com.activityartapp.presentation.MainViewEvent.*
import com.activityartapp.util.ParcelableActivity
import com.activityartapp.util.Response.*
import com.activityartapp.util.parcelize
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.Year
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val activitiesCache: ActivitiesCache,
    private val getAccessTokenFromDiskOrRemote: GetAccessTokenFromDiskOrRemote,
    private val savedStateHandle: SavedStateHandle,
    private val insertActivitiesIntoMemory: InsertActivitiesIntoMemory
) : BaseRoutingViewModel<
        MainViewState,
        MainViewEvent,
        MainDestination>() {

    companion object {
        private const val ACTIVITIES_KEY = "activities"
    }

    var athleteId: Long? = null
    var accessToken: String? = null

    init {
        val activities: List<ParcelableActivity>? = savedStateHandle[ACTIVITIES_KEY]
        if (activities != null) {
            insertActivitiesIntoMemory(activities)
        }
    }

    override fun onEvent(event: MainViewEvent) {
        when (event) {
            is LoadAuthentication -> onLoadAuthentication(event)
            is LoadedActivities -> onLoadedActivities()
        }
    }

    private fun onLoadAuthentication(event: LoadAuthentication) {
        viewModelScope.launch {
            pushState(
                when (val response = getAccessTokenFromDiskOrRemote(event.uri)) {
                    is Success -> Authenticated
                    is Error -> if (response.data != null) {
                        Authenticated
                    } else {
                        Unauthenticated
                    }
                }
            )
        }
    }

    private fun onLoadedActivities() {
        activitiesCache
            .cachedActivities
            ?.parcelize()
            ?.let { savedStateHandle[ACTIVITIES_KEY] = it }
    }
}