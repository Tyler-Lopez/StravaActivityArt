@file:OptIn(ExperimentalPagerApi::class)

package com.company.activityart.presentation.edit_art_screen

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Parcel
import android.os.Parcelable
import android.util.Size
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

    data class Loading(
        override val pagerStateWrapper: PagerStateWrapper
    ) : EditArtViewState

    data class Standby(
        val filterStateWrapper: FilterStateWrapper,
        override val pagerStateWrapper: PagerStateWrapper,
        val size: Size,
        val styleWrapper: StyleWrapper
    ) : EditArtViewState
}

@Parcelize
data class PagerStateWrapper(
    val pagerHeaders: List<EditArtHeaderType>,
    val pagerState: @RawValue PagerState, // Todo, evaluate whether RawValue works
    val fadeLengthMs: Int,
) : Parcelable

@Parcelize
data class FilterStateWrapper(
    val excludedActivityTypes: Set<String> = setOf(),
    val unixSecondSelectedStart: Long,
    val unixSecondSelectedEnd: Long
) : Parcelable

@Parcelize
data class StyleWrapper(
    val background: ColorWrapper
) : Parcelable

@Parcelize
data class ColorWrapper(
    val alpha: Int,
    val blue: Int,
    val green: Int,
    val red: Int
) : Parcelable {

    companion object {
        private const val VALUE_NONE = 0
        private const val VALUE_MAX = 255
        val VALUE_RANGE = VALUE_NONE..VALUE_MAX
    }

}