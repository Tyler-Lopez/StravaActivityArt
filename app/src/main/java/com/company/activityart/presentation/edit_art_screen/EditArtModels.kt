@file:OptIn(ExperimentalPagerApi::class)

package com.company.activityart.presentation.edit_art_screen

import android.graphics.Bitmap
import android.os.Parcelable
import android.util.Size
import com.company.activityart.R
import com.company.activityart.architecture.ViewEvent
import com.company.activityart.architecture.ViewState
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

sealed interface EditArtViewEvent : ViewEvent {

    object MakeFullscreenClicked : EditArtViewEvent
    object NavigateUpClicked : EditArtViewEvent
    data class PageHeaderClicked(val position: Int) : EditArtViewEvent
    object SaveClicked : EditArtViewEvent

    sealed interface ArtMutatingEvent : EditArtViewEvent {
        data class FilterDateChanged(
            val newUnixSecondStart: Long,
            val newUnixSecondEnd: Long
        ) : ArtMutatingEvent

        sealed interface FilterTypeChanged : ArtMutatingEvent {
            val type: String

            data class FilterTypeAdded(override val type: String) : FilterTypeChanged
            data class FilterTypeRemoved(override val type: String) : FilterTypeChanged
        }

        data class ScreenMeasured(val width: Int, val height: Int) : ArtMutatingEvent
        data class StylesColorChanged(
            val styleType: StyleType,
            val colorType: ColorType,
            val changedTo: Float
        ) : ArtMutatingEvent

        data class StylesStrokeWidthChanged(val changedTo: StrokeWidthType) : ArtMutatingEvent
    }
}

sealed interface EditArtViewState : ViewState {
    val pagerStateWrapper: PagerStateWrapper

    data class Loading(
        override val pagerStateWrapper: PagerStateWrapper
    ) : EditArtViewState

    data class Standby(
        val bitmap: Bitmap?,
        val filterStateWrapper: FilterStateWrapper,
        override val pagerStateWrapper: PagerStateWrapper,
        val sizeActual: Size,
        val sizeMaximum: Size,
        val styleActivities: ColorWrapper,
        val styleBackground: ColorWrapper,
        val styleStrokeWidthType: StrokeWidthType
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
data class ColorWrapper(
    val alpha: Float,
    val blue: Float,
    val green: Float,
    val red: Float
) : Parcelable {

    companion object {
        private const val VALUE_NONE = 0f
        private const val VALUE_MAX = 1f
        val VALUE_RANGE = VALUE_NONE..VALUE_MAX
        val INITIAL_BG_COLOR =
            ColorWrapper(VALUE_NONE, VALUE_NONE, VALUE_NONE, VALUE_NONE)
    }
}

enum class StyleType {
    BACKGROUND,
    ACTIVITIES,
}

enum class ColorType {
    RED,
    GREEN,
    BLUE,
    ALPHA
}

enum class StrokeWidthType(val headerId: Int) {
    THIN(R.string.edit_art_style_stroke_thin),
    MEDIUM(R.string.edit_art_style_stroke_medium),
    THICK(R.string.edit_art_style_stroke_thick);
}