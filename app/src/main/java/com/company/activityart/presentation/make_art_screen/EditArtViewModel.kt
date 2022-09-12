package com.company.activityart.presentation.make_art_screen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.company.activityart.architecture.BaseRoutingViewModel
import com.company.activityart.domain.models.Activity
import com.company.activityart.domain.use_case.activities.GetActivitiesFromCacheUseCase
import com.company.activityart.presentation.MainDestination
import com.company.activityart.presentation.MainDestination.*
import com.company.activityart.presentation.make_art_screen.EditArtViewState.*
import com.company.activityart.presentation.make_art_screen.EditArtViewEvent.*
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.Year
import javax.inject.Inject

@HiltViewModel
@OptIn(ExperimentalPagerApi::class)
class EditArtViewModel @Inject constructor(
    private val activitiesFromCacheUseCase: GetActivitiesFromCacheUseCase,
    savedStateHandle: SavedStateHandle
) : BaseRoutingViewModel<EditArtViewState, EditArtViewEvent, MainDestination>() {

    companion object {
        private const val NO_ACTIVITIES_LOADED_COUNT = 0
        private const val YEAR_START = 2018
        private val YEAR_NOW = Year.now().value

        private const val INITIAL_POSITION = 0
    }

    private val activitiesByYear: Map<Int, List<Activity>> = activitiesFromCacheUseCase()
    private val pagerHeaders: List<EditArtHeaderType> = EditArtHeaderType.values().toList()
    private val pagerState: PagerState = PagerState(pagerHeaders.size)

    init {
        pushState(
            Standby(
                activitiesByYear = activitiesByYear,
                pageHeaders = pagerHeaders,
                pagerState = pagerState,
                newPosition = INITIAL_POSITION
            )
        )
        viewModelScope.launch {
            getActivities()
        }
    }

    override fun onEvent(event: EditArtViewEvent) {
        viewModelScope.launch {
            when (event) {
                is MakeFullscreenClicked -> onMakeFullscreenClicked()
                is NavigateUpClicked -> onNavigateUpClicked()
                is PageHeaderClicked -> onPageHeaderClicked(event)
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

    private fun onPageHeaderClicked(event: PageHeaderClicked) {
        pushState(
            Standby(
                activitiesByYear,
                pagerHeaders,
                pagerState,
                event.position
            )
        )
    }

    private fun onSaveClicked() {

    }

    private fun onSelectFiltersClicked() {

    }

    private fun onSelectStylesClicked() {

    }

    private fun getActivities() {
        /*
        pushState(
            Standby(
                null,
                activitiesFromCacheUseCase()[2022]?.size ?: 0
            )
        )

         */
    }

}