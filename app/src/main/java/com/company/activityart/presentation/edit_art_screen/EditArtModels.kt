@file:OptIn(ExperimentalPagerApi::class)

package com.company.activityart.presentation.edit_art_screen

import android.graphics.Bitmap
import android.os.Parcelable
import android.util.Size
import androidx.annotation.Px
import androidx.compose.foundation.ScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
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
    sealed interface SizeCustomChanged : EditArtViewEvent {
        val changedToPx: Int

        data class HeightChanged(override val changedToPx: Int) : SizeCustomChanged
        data class WidthChanged(override val changedToPx: Int) : SizeCustomChanged
    }

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

        data class SizeChanged(val changedIndex: Int) : ArtMutatingEvent
        sealed interface SizeCustomChangeDone : ArtMutatingEvent {
            object HeightChanged : SizeCustomChangeDone
            object WidthChanged : SizeCustomChangeDone
        }

        data class SizeRotated(val rotatedIndex: Int) : ArtMutatingEvent
        data class ScreenMeasured(@Px val width: Int, @Px val height: Int) : ArtMutatingEvent
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
        val scrollStateStyle: ScrollState,
        val scrollStateResize: ScrollState,
        val sizeActual: Size,
        val sizeResolutionList: List<Resolution>,
        val sizeResolutionListSelectedIndex: Int,
        val sizeCustomWidthPx: Int,
        val sizeCustomHeightPx: Int,
        val sizeCustomRangePx: IntRange,
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
            ColorWrapper(VALUE_MAX, VALUE_NONE, VALUE_NONE, VALUE_NONE)
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


sealed interface Resolution {

    val widthPx: Int
    val heightPx: Int
    val stringResourceId: Int

    @Composable
    fun displayTextResolution(): String

    companion object {
        private const val DEFAULT_CUSTOM_WIDTH_PX = 1000
        private const val DEFAULT_CUSTOM_HEIGHT_PX = 1000
    }

    interface SwappableResolution : Resolution {
        var swapWidthWithHeight: Boolean
        val swappingChangesSize: Boolean
            get() = widthPx != heightPx
        val origWidthPx: Int
        val origHeightPx: Int

        override val heightPx: Int
            get() = if (swapWidthWithHeight) origWidthPx else origHeightPx

        override val widthPx: Int
            get() = if (swapWidthWithHeight) origHeightPx else origWidthPx

        @Composable
        fun displayTextPixels(): String {
            return stringResource(
                R.string.edit_art_resize_pixels_placeholder,
                if (swapWidthWithHeight) origHeightPx else origWidthPx,
                if (swapWidthWithHeight) origWidthPx else origHeightPx,
            )
        }
    }

    data class ComputerResolution(
        override val stringResourceId: Int,
        override val origWidthPx: Int,
        override val origHeightPx: Int,
        override var swapWidthWithHeight: Boolean = false
    ) : SwappableResolution {
        @Composable
        override fun displayTextResolution(): String {
            return stringResource(stringResourceId)
        }
    }

    data class PrintResolution(
        override val origWidthPx: Int,
        override val origHeightPx: Int,
        val widthMeasurementValue: Int,
        val heightMeasurementValue: Int,
        override var swapWidthWithHeight: Boolean = false
    ) : SwappableResolution {

        companion object {
            private const val MEASUREMENT_UNIT: String = "\""
        }

        @Composable
        override fun displayTextResolution(): String {
            return stringResource(
                stringResourceId,
                if (swapWidthWithHeight) heightMeasurementValue else widthMeasurementValue,
                MEASUREMENT_UNIT,
                if (swapWidthWithHeight) widthMeasurementValue else heightMeasurementValue,
                MEASUREMENT_UNIT
            )
        }

        override val stringResourceId: Int
            get() = R.string.edit_art_resize_option_print
    }

    data class CustomResolution(
        override val stringResourceId: Int,
        var customWidthPx: Int = DEFAULT_CUSTOM_WIDTH_PX,
        var customHeightPx: Int = DEFAULT_CUSTOM_HEIGHT_PX,
    ) : Resolution {
        override val widthPx: Int
            get() = customWidthPx
        override val heightPx: Int
            get() = customHeightPx

        @Composable
        override fun displayTextResolution(): String {
            return stringResource(stringResourceId)
        }
    }
}
