@file:OptIn(ExperimentalPagerApi::class)

package com.company.activityart.presentation.edit_art_screen

import android.os.Parcelable
import com.company.activityart.architecture.ViewEvent
import com.company.activityart.architecture.ViewState
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

sealed class EditArtViewEvent : ViewEvent {
    data class FilterDateChanged(
        val newUnixSecondStart: Float,
        val newUnixSecondEnd: Float
    ) : EditArtViewEvent()

    object MakeFullscreenClicked : EditArtViewEvent()
    object NavigateUpClicked : EditArtViewEvent()
    data class PageHeaderClicked(val position: Int) : EditArtViewEvent()
    object SaveClicked : EditArtViewEvent()
    object SelectFiltersClicked : EditArtViewEvent()
    object SelectStylesClicked : EditArtViewEvent()
}

data class EditArtViewState(
    val filterStateWrapper: FilterStateWrapper,
    val pagerStateWrapper: PagerStateWrapper,
) : ViewState

@Parcelize
data class PagerStateWrapper(
    val pagerHeaders: List<EditArtHeaderType>,
    val pagerState: @RawValue PagerState,
    val pagerNewPosition: Int,
) : Parcelable

@Parcelize
data class FilterStateWrapper(
    val unixSecondSelectedStart: Float,
    val unixSecondSelectedEnd: Float,
    val unixSecondTotalStart: Float,
    val unixSecondTotalEnd: Float
) : Parcelable