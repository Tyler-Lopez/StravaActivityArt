package com.company.activityart.presentation.make_art_screen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.company.activityart.architecture.BaseRoutingViewModel
import com.company.activityart.domain.models.Activity
import com.company.activityart.domain.use_case.activities.GetActivitiesByYearFromCacheUseCase
import com.company.activityart.domain.use_case.activities.GetActivitiesFromCacheUseCase
import com.company.activityart.presentation.MainDestination
import com.company.activityart.presentation.MainDestination.*
import com.company.activityart.presentation.make_art_screen.MakeArtViewState.*
import com.company.activityart.presentation.make_art_screen.MakeArtViewEvent.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.Year
import javax.inject.Inject

@HiltViewModel
class MakeArtViewModel @Inject constructor(
    private val activitiesFromCacheUseCase: GetActivitiesFromCacheUseCase,
    savedStateHandle: SavedStateHandle
) : BaseRoutingViewModel<MakeArtViewState, MakeArtViewEvent, MainDestination>() {

    companion object {
        private const val NO_ACTIVITIES_LOADED_COUNT = 0
        private const val YEAR_START = 2018
        private val YEAR_NOW = Year.now().value
    }

    val activitiesByYear: Map<Int, List<Activity>> = mutableMapOf()

    init {
        Loading.push()
        viewModelScope.launch {
            getActivities()
        }
    }

    override fun onEvent(event: MakeArtViewEvent) {
        viewModelScope.launch {
            when (event) {
                is MakeFullscreenClicked -> onMakeFullscreenClicked()
                is NavigateUpClicked -> onNavigateUpClicked()
                is SaveClicked -> onSaveClicked()
                is SelectFiltersClicked -> onSelectFiltersClicked()
                is SelectStylesClicked -> onSelectStylesClicked()
            }
        }
    }

    private fun onMakeFullscreenClicked() {

    }

    private fun onNavigateUpClicked() {
        routeTo(NavigateUp)
    }

    private fun onSaveClicked() {

    }

    private fun onSelectFiltersClicked() {

    }

    private fun onSelectStylesClicked() {

    }

    private fun getActivities() {
        pushState(
            Standby(
                null,
                activitiesFromCacheUseCase()[2022]?.size ?: 0
            )
        )
    }

}