package com.company.activityart.presentation.edit_art_screen

import androidx.annotation.Px
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.company.activityart.architecture.BaseRoutingViewModel
import com.company.activityart.domain.use_case.activities.GetActivitiesFromCacheUseCase
import com.company.activityart.presentation.MainDestination
import com.company.activityart.presentation.MainDestination.*
import com.company.activityart.presentation.edit_art_screen.EditArtViewEvent.*
import com.company.activityart.presentation.edit_art_screen.EditArtViewState.*
import com.company.activityart.util.TimeUtils
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.Year
import javax.inject.Inject

@HiltViewModel
@OptIn(ExperimentalPagerApi::class)
class EditArtViewModel @Inject constructor(
    private val activitiesFromCacheUseCase: GetActivitiesFromCacheUseCase,
    private val timeUtils: TimeUtils,
    savedStateHandle: SavedStateHandle
) : BaseRoutingViewModel<EditArtViewState, EditArtViewEvent, MainDestination>() {

    companion object {
        @Px
        private const val INITIAL_HEIGHT_PX = 1080f

        private const val INITIAL_PAGE_POSITION = 0

        @Px
        private const val INITIAL_WIDTH_PX = 1920f
    }


    init {
        val pagerHeaders: List<EditArtHeaderType> = EditArtHeaderType.values().toList()
        val pagerState = PagerState(pagerHeaders.size)
        val pagerStateWrapper = PagerStateWrapper(
            pagerHeaders,
            pagerState,
            INITIAL_PAGE_POSITION
        )
        pushState(Loading(pagerStateWrapper = pagerStateWrapper))
        viewModelScope.launch(Dispatchers.Default) {
            val activities = activitiesFromCacheUseCase().flatMap { it.value }
            val activitiesUnixSeconds =
                activities.map { timeUtils.iso8601StringToUnixSecond(it.iso8601LocalDate) }

            val unixSecondFirst = activitiesUnixSeconds.min()
            val unixSecondLast = activitiesUnixSeconds.max()

            pushState(
                Standby(
                    filterExcludeActivityTypes = setOf(),
                    filterStateWrapper = FilterStateWrapper(
                        unixSecondSelectedStart = unixSecondFirst,
                        unixSecondSelectedEnd = unixSecondLast
                    ),
                    pagerStateWrapper = pagerStateWrapper,
                    sizeWrapper = SizeWrapper(
                        INITIAL_WIDTH_PX,
                        INITIAL_HEIGHT_PX
                    )
                )
            )
        }
    }

    override fun onEvent(event: EditArtViewEvent) {
        viewModelScope.launch {
            when (event) {
                is FilterDateChanged -> onFilterDateChanged(event)
                is FilterTypeChanged -> onFilterTypeChanged(event)
                is MakeFullscreenClicked -> onMakeFullscreenClicked()
                is NavigateUpClicked -> onNavigateUpClicked()
                is PageHeaderClicked -> onPageHeaderClicked(event)
                is SaveClicked -> onSaveClicked()
                is SelectFiltersClicked -> onSelectFiltersClicked()
                is SelectStylesClicked -> onSelectStylesClicked()
            }
        }
    }

    private fun onFilterDateChanged(event: FilterDateChanged) {
        (lastPushedState as? Standby)?.run {
            copy(
                filterStateWrapper = filterStateWrapper.copy(
                    unixSecondSelectedStart = event.newUnixSecondStart,
                    unixSecondSelectedEnd = event.newUnixSecondEnd
                )
            )
        }?.push()
    }

    private fun onFilterTypeChanged(event: FilterTypeChanged) {
    }

    private fun onMakeFullscreenClicked() {

    }

    private fun onNavigateUpClicked() {
        routeTo(NavigateUp)
    }

    private fun onPageHeaderClicked(event: PageHeaderClicked) {
        (lastPushedState as? Standby)?.run {
            copy(
                pagerStateWrapper = pagerStateWrapper.copy(
                    pagerNewPosition = event.position
                )
            )
        }?.push()
    }

    private fun onSaveClicked() {

    }

    private fun onSelectFiltersClicked() {

    }

    private fun onSelectStylesClicked() {

    }
}