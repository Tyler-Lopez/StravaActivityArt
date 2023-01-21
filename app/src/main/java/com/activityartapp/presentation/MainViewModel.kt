package com.activityartapp.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.activityartapp.architecture.BaseRoutingViewModel
import com.activityartapp.data.cache.ActivitiesCache
import com.activityartapp.domain.use_case.authentication.GetAccessTokenUseCase
import com.activityartapp.presentation.MainViewState.*
import com.activityartapp.presentation.MainViewEvent.*
import com.activityartapp.util.ParcelableActivity
import com.activityartapp.util.Response.*
import com.activityartapp.util.parcelize
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.net.UnknownHostException
import java.time.Year
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val activitiesCache: ActivitiesCache,
    private val getAccessTokenUseCase: GetAccessTokenUseCase,
    val savedStateHandle: SavedStateHandle
) : BaseRoutingViewModel<
        MainViewState,
        MainViewEvent,
        MainDestination>() {

    companion object {
        private val YEAR_NOW = Year.now().value
        private const val YEAR_EARLIEST = 2018
    }
    var athleteId: Long? = null
    var accessToken: String? = null

    init {
        (YEAR_EARLIEST..YEAR_NOW).forEach { year ->
            val yearActivities: List<ParcelableActivity>? = savedStateHandle["$year"]
            yearActivities?.map { it }?.let {
                activitiesCache.cachedActivitiesByYear[year] = it
            }
        }
    }

    override fun onEvent(event: MainViewEvent) {
        when (event) {
            is LoadAuthentication -> onLoadAuthentication(event)
            is MakeArt -> onMakeArt()
        }
    }

    private fun onLoadAuthentication(event: LoadAuthentication) {
        viewModelScope.launch {
            pushState(
                when (val response = getAccessTokenUseCase(event.uri)) {
                    is Success -> Authenticated
                    is Error -> if (response.exception is UnknownHostException) {
                        Authenticated
                    } else {
                        Unauthenticated
                    }
                }
            )
        }
    }

    private fun onMakeArt() {
        (YEAR_EARLIEST..YEAR_NOW).forEach { year ->
            (activitiesCache.cachedActivitiesByYear[year])
                ?.parcelize()
                ?.let {
                    println("Put ${it.size} activities into $year instance state")
                    savedStateHandle["$year"] = it
                }
        }
    }
}