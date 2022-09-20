@file:OptIn(ExperimentalPagerApi::class)

package com.company.activityart.presentation.edit_art_screen

import android.os.Parcelable
import androidx.annotation.Px
import com.company.activityart.architecture.ViewEvent
import com.company.activityart.architecture.ViewState
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

sealed interface EditArtViewEvent : ViewEvent {
    data class FilterDateChanged(
        val newUnixSecondStart: Long,
        val newUnixSecondEnd: Long
    ) : EditArtViewEvent

    sealed interface FilterTypeChanged : EditArtViewEvent {
        val type: String

        data class FilterTypeAdded(override val type: String) : FilterTypeChanged
        data class FilterTypeRemoved(override val type: String) : FilterTypeChanged
    }

    object MakeFullscreenClicked : EditArtViewEvent
    object NavigateUpClicked : EditArtViewEvent
    data class PageHeaderClicked(val position: Int) : EditArtViewEvent
    object SaveClicked : EditArtViewEvent
    object SelectFiltersClicked : EditArtViewEvent
    object SelectStylesClicked : EditArtViewEvent
}

sealed interface EditArtViewState : ViewState {
    val pagerStateWrapper: PagerStateWrapper

    data class Loading(override val pagerStateWrapper: PagerStateWrapper) : EditArtViewState
    data class Standby(
        val filterExcludeActivityTypes: Set<String>,
        val filterStateWrapper: FilterStateWrapper,
        override val pagerStateWrapper: PagerStateWrapper,
        val sizeWrapper: SizeWrapper
    ) : EditArtViewState
}

@Parcelize
data class PagerStateWrapper(
    val pagerHeaders: List<EditArtHeaderType>,
    val pagerState: @RawValue PagerState, // Todo, evaluate whether RawValue works
    val pagerNewPosition: Int,
) : Parcelable

@Parcelize
data class FilterStateWrapper(
    val unixSecondSelectedStart: Long,
    val unixSecondSelectedEnd: Long
) : Parcelable

@Parcelize
data class SizeWrapper(
    @Px val heightPx: Float,
    @Px val widthPx: Float
) : Parcelable