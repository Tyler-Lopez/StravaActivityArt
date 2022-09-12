package com.company.activityart.presentation.make_art_screen

import com.company.activityart.architecture.ViewEvent
import com.company.activityart.architecture.ViewState
import com.company.activityart.domain.models.Activity
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState

sealed class EditArtViewEvent : ViewEvent {
    object MakeFullscreenClicked : EditArtViewEvent()
    object NavigateUpClicked : EditArtViewEvent()
    data class PageHeaderClicked(val position: Int) : EditArtViewEvent()
    object SaveClicked : EditArtViewEvent()
    object SelectFiltersClicked : EditArtViewEvent()
    object SelectStylesClicked : EditArtViewEvent()
}

@OptIn(ExperimentalPagerApi::class)
sealed class EditArtViewState(
    open val pageHeaders: List<EditArtHeaderType>,
    open val pagerState: PagerState,
    open val newPosition: Int
) : ViewState {

    data class Standby(
        val activitiesByYear: Map<Int, List<Activity>>,
        override val pageHeaders: List<EditArtHeaderType>,
        override val pagerState: PagerState,
        override val newPosition: Int
    ) : EditArtViewState(pageHeaders, pagerState, newPosition)
}