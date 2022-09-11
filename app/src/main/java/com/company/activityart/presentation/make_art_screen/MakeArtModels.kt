package com.company.activityart.presentation.make_art_screen

import android.graphics.Bitmap
import com.company.activityart.architecture.ViewEvent
import com.company.activityart.architecture.ViewState
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import kotlinx.coroutines.CoroutineScope

sealed class MakeArtViewEvent : ViewEvent {
    object MakeFullscreenClicked : MakeArtViewEvent()
    object NavigateUpClicked : MakeArtViewEvent()
    data class PageHeaderClicked(val position: Int) : MakeArtViewEvent()
    object SaveClicked : MakeArtViewEvent()
    object SelectFiltersClicked : MakeArtViewEvent()
    object SelectStylesClicked : MakeArtViewEvent()
}

@OptIn(ExperimentalPagerApi::class)
sealed class MakeArtViewState(
    open val pageHeaders: List<MakeArtHeaderType>,
    open val pagerState: PagerState,
    open val newPosition: Int
) : ViewState {

    data class Loading(
        override val pageHeaders: List<MakeArtHeaderType>,
        override val pagerState: PagerState,
        override val newPosition: Int
    ) : MakeArtViewState(pageHeaders, pagerState, newPosition)

    data class Standby(
        val artPreview: Bitmap?,
        override val pageHeaders: List<MakeArtHeaderType>,
        override val pagerState: PagerState,
        override val newPosition: Int
    ) : MakeArtViewState(pageHeaders, pagerState, newPosition)

}