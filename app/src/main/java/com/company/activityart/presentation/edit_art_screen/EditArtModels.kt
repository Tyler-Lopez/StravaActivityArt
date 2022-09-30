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
import com.company.activityart.util.classes.YearMonthDay
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

sealed interface EditArtViewEvent : ViewEvent {

    object DialogNavigateUpCancelled : EditArtViewEvent
    object DialogNavigateUpConfirmed : EditArtViewEvent
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
        sealed interface FilterDateChanged : ArtMutatingEvent {
            val changedTo: YearMonthDay

            data class FilterAfterChanged(override val changedTo: YearMonthDay) : FilterDateChanged
            data class FilterBeforeChanged(override val changedTo: YearMonthDay) : FilterDateChanged
        }

        sealed interface FilterTypeChanged : ArtMutatingEvent {
            val type: String

            data class FilterTypeAdded(override val type: String) : FilterTypeChanged
            data class FilterTypeRemoved(override val type: String) : FilterTypeChanged
        }

        data class SizeChanged(val changedIndex: Int) : ArtMutatingEvent
        object SizeCustomChangeDone : ArtMutatingEvent
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

    val dialogNavigateUpActive: Boolean
    val pagerStateWrapper: PagerStateWrapper

    data class Loading(
        override val dialogNavigateUpActive: Boolean,
        override val pagerStateWrapper: PagerStateWrapper
    ) : EditArtViewState

    data class Standby(
        val bitmap: Bitmap?,
        override val dialogNavigateUpActive: Boolean,
        val filterDateMaxDateSelectedYearMonthDay: YearMonthDay,
        val filterDateMinDateSelectedYearMonthDay: YearMonthDay,
        val filterDateMaxDateTotalYearMonthDay: YearMonthDay,
        val filterDateMinDateTotalYearMonthDay: YearMonthDay,
        override val pagerStateWrapper: PagerStateWrapper,
        val scrollStateFilter: ScrollState,
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
        private const val EIGHT_BIT_CHANNEL_LIMIT = 255
        val VALUE_RANGE = VALUE_NONE..VALUE_MAX
    }

    val redAsEightBit get() = red.toEightBitRepresentation()
    val greenAsEightBit get() = green.toEightBitRepresentation()
    val blueAsEightBit get() = blue.toEightBitRepresentation()
    val alphaAsEightBit get() = alpha.toEightBitRepresentation()

    private fun Float.toEightBitRepresentation(): Int {
        return (this * EIGHT_BIT_CHANNEL_LIMIT).toInt()
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

    interface RotatingResolution : Resolution {
        val isRotated: Boolean
        val swappingChangesSize: Boolean
            get() = widthPx != heightPx
        val origWidthPx: Int
        val origHeightPx: Int

        override val heightPx: Int
            get() = if (isRotated) origWidthPx else origHeightPx

        override val widthPx: Int
            get() = if (isRotated) origHeightPx else origWidthPx

        fun copyWithRotation(): RotatingResolution

        @Composable
        fun displayTextPixels(): String {
            return stringResource(
                R.string.edit_art_resize_pixels_placeholder,
                if (isRotated) origHeightPx else origWidthPx,
                if (isRotated) origWidthPx else origHeightPx,
            )
        }
    }

    data class ComputerResolution(
        override val stringResourceId: Int,
        override val origWidthPx: Int,
        override val origHeightPx: Int,
        override val isRotated: Boolean = false
    ) : RotatingResolution {
        @Composable
        override fun displayTextResolution(): String {
            return stringResource(stringResourceId)
        }

        override fun copyWithRotation(): RotatingResolution {
            return copy(isRotated = !isRotated)
        }
    }

    data class PrintResolution(
        override val origWidthPx: Int,
        override val origHeightPx: Int,
        val widthMeasurementValue: Int,
        val heightMeasurementValue: Int,
        override val isRotated: Boolean = false
    ) : RotatingResolution {

        companion object {
            private const val MEASUREMENT_UNIT: String = "\""
        }

        @Composable
        override fun displayTextResolution(): String {
            return stringResource(
                stringResourceId,
                if (isRotated) heightMeasurementValue else widthMeasurementValue,
                MEASUREMENT_UNIT,
                if (isRotated) widthMeasurementValue else heightMeasurementValue,
                MEASUREMENT_UNIT
            )
        }

        override fun copyWithRotation(): RotatingResolution {
            return copy(isRotated = !isRotated)
        }

        override val stringResourceId: Int
            get() = R.string.edit_art_resize_option_print
    }

    data class CustomResolution(
        val customWidthPx: Int = DEFAULT_CUSTOM_WIDTH_PX,
        val customHeightPx: Int = DEFAULT_CUSTOM_HEIGHT_PX,
    ) : Resolution {

        override val stringResourceId: Int
            get() = R.string.edit_art_resize_option_custom
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
