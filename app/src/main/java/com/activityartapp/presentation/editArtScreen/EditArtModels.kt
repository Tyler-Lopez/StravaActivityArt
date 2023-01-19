@file:OptIn(ExperimentalPagerApi::class)

package com.activityartapp.presentation.editArtScreen

import android.graphics.Bitmap
import android.os.Parcelable
import android.util.Size
import androidx.annotation.StringRes
import androidx.compose.foundation.ScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.room.Ignore
import com.activityartapp.R
import com.activityartapp.architecture.ViewEvent
import com.activityartapp.architecture.ViewState
import com.activityartapp.domain.models.ResolutionListFactory
import com.activityartapp.presentation.editArtScreen.subscreens.resize.ResolutionListFactoryImpl
import com.activityartapp.presentation.editArtScreen.subscreens.type.EditArtTypeSection
import com.activityartapp.presentation.editArtScreen.subscreens.type.EditArtTypeType
import com.activityartapp.util.FontSizeType
import com.activityartapp.util.enums.FontType
import com.activityartapp.util.enums.FontWeightType
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
import javax.inject.Inject

annotation class UnixMS

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

        sealed interface FilterChanged : ArtMutatingEvent {
            val filterType: EditArtFilterType

            data class FilterDateSelectionChanged(val index: Int) : FilterChanged {
                override val filterType: EditArtFilterType
                    get() = EditArtFilterType.DATE
            }

            sealed interface FilterDateCustomChanged : FilterChanged {
                val changedTo: Long
                override val filterType: EditArtFilterType
                    get() = EditArtFilterType.DATE

                data class FilterAfterChanged(@UnixMS override val changedTo: Long) :
                    FilterDateCustomChanged

                data class FilterBeforeChanged(@UnixMS override val changedTo: Long) :
                    FilterDateCustomChanged
            }

            data class FilterDistanceChanged(val changedTo: ClosedFloatingPointRange<Double>) :
                FilterChanged {
                override val filterType: EditArtFilterType
                    get() = EditArtFilterType.DISTANCE
            }

            data class FilterTypeToggled(val type: String) : FilterChanged {
                override val filterType: EditArtFilterType
                    get() = EditArtFilterType.TYPE
            }
        }

        data class SizeChanged(val changedIndex: Int) : ArtMutatingEvent
        object SizeCustomChangeDone : ArtMutatingEvent
        data class SizeRotated(val rotatedIndex: Int) : ArtMutatingEvent
        data class StyleColorFontUseCustomChanged(val useCustom: Boolean) : ArtMutatingEvent
        data class StylesColorChanged(
            val styleType: StyleType,
            val colorType: ColorType,
            val changedTo: Float
        ) : ArtMutatingEvent

        data class StylesStrokeWidthChanged(val changedTo: StrokeWidthType) : ArtMutatingEvent
        data class TypeCustomTextChanged(
            val section: EditArtTypeSection,
            val changedTo: String
        ) : ArtMutatingEvent

        data class TypeFontChanged(val changedTo: FontType) : ArtMutatingEvent
        data class TypeFontWeightChanged(val changedTo: FontWeightType) : ArtMutatingEvent
        data class TypeFontItalicChanged(val changedTo: Boolean) : ArtMutatingEvent
        data class TypeFontSizeChanged(val changedTo: FontSizeType) : ArtMutatingEvent
        data class TypeSelectionChanged(
            val section: EditArtTypeSection,
            val typeSelected: EditArtTypeType
        ) : ArtMutatingEvent
    }
}

sealed interface EditArtViewState : ViewState {

    companion object {
        private const val FADE_LENGTH_MS = 1000
    }

    val dialogNavigateUpActive: Boolean
    val pagerStateWrapper: PagerStateWrapper

    data class Loading(
        override val dialogNavigateUpActive: Boolean = false,
        override val pagerStateWrapper: PagerStateWrapper = PagerStateWrapper(
            pagerHeaders = EditArtHeaderType.values().toList(),
            pagerState = PagerState(EditArtHeaderType.values().toList().size),
            fadeLengthMs = FADE_LENGTH_MS // todo
        )
    ) : EditArtViewState

    /**
     * @param styleActivities The color of the activities on the art.
     * @param styleBackground The color of the background of the art.
     * @param styleFont The color of text on the art. When set to null, [styleActivities] determines
     * the color of the text.
     */
    @Parcelize
    data class Standby(
        @IgnoredOnParcel val bitmap: Bitmap? = null,
        @IgnoredOnParcel override val dialogNavigateUpActive: Boolean = false,
        val filterActivitiesCountDate: Int = -1,
        val filterActivitiesCountDistance: Int = -1,
        val filterActivitiesCountType: Int = -1,
        val filterDateSelections: List<DateSelection>? = null,
        val filterDateSelectionIndex: Int = INIT_SELECTION_INDEX,
        val filterDistanceSelectedStart: Double? = null,
        val filterDistanceSelectedEnd: Double? = null,
        val filterDistanceTotalStart: Double? = null,
        val filterDistanceTotalEnd: Double? = null,
        val filterTypes: Map<String, Boolean>? = null,
        @IgnoredOnParcel override val pagerStateWrapper: PagerStateWrapper = PagerStateWrapper(
            pagerHeaders = EditArtHeaderType.values().toList(),
            pagerState = PagerState(EditArtHeaderType.values().toList().size),
            fadeLengthMs = FADE_LENGTH_MS
        ),
        @IgnoredOnParcel val scrollStateFilter: ScrollState = ScrollState(INITIAL_SCROLL_STATE),
        @IgnoredOnParcel val scrollStateStyle: ScrollState = ScrollState(INITIAL_SCROLL_STATE),
        @IgnoredOnParcel val scrollStateType: ScrollState = ScrollState(INITIAL_SCROLL_STATE),
        @IgnoredOnParcel val scrollStateResize: ScrollState = ScrollState(INITIAL_SCROLL_STATE),
        val sizeResolutionList: List<Resolution> = ResolutionListFactoryImpl().create(),
        val sizeResolutionListSelectedIndex: Int = INITIAL_SELECTED_RES_INDEX,
        @IgnoredOnParcel val sizeCustomMaxPx: Int = CUSTOM_SIZE_MAXIMUM_PX,
        @IgnoredOnParcel val sizeCustomMinPx: Int = CUSTOM_SIZE_MINIMUM_PX,
        val styleActivities: ColorWrapper = ColorWrapper(
            alpha = INIT_ACTIVITIES_ALPHA,
            blue = INIT_ACTIVITIES_BLUE,
            green = INIT_ACTIVITIES_GREEN,
            red = INIT_ACTIVITIES_RED
        ),
        val styleBackground: ColorWrapper = ColorWrapper(
            alpha = INIT_BACKGROUND_ALPHA,
            blue = INIT_BACKGROUND_BLUE,
            green = INIT_BACKGROUND_GREEN,
            red = INIT_BACKGROUND_RED
        ),
        val styleFont: ColorWrapper? = null,
        val styleStrokeWidthType: StrokeWidthType = INIT_STROKE_WIDTH,
        val typeActivitiesDistanceMetersSummed: Int = -1,
        val typeFontSelected: FontType = INIT_TYPE_FONT_SELECTION,
        val typeFontWeightSelected: FontWeightType = INIT_TYPE_FONT_WEIGHT_SELECTION,
        val typeFontItalicized: Boolean = INIT_TYPE_IS_ITALICIZED,
        val typeFontSizeSelected: FontSizeType = INIT_TYPE_FONT_SIZE_SELECTION,
        @IgnoredOnParcel val typeMaximumCustomTextLength: Int = CUSTOM_TEXT_MAXIMUM_LENGTH,
        val typeLeftSelected: EditArtTypeType = INIT_TYPE_TYPE,
        val typeLeftCustomText: String = INIT_TYPE_CUSTOM_TEXT,
        val typeCenterSelected: EditArtTypeType = INIT_TYPE_TYPE,
        val typeCenterCustomText: String = INIT_TYPE_CUSTOM_TEXT,
        val typeRightSelected: EditArtTypeType = INIT_TYPE_TYPE,
        val typeRightCustomText: String = INIT_TYPE_CUSTOM_TEXT,
    ) : EditArtViewState, Parcelable {

        companion object {
            private const val CUSTOM_SIZE_MINIMUM_PX = 100
            private const val CUSTOM_SIZE_MAXIMUM_PX = 12000

            private const val INITIAL_SCROLL_STATE = 0
            private const val INITIAL_SELECTED_RES_INDEX = 0

            /** Initial Style settings **/
            private const val INIT_ACTIVITIES_ALPHA = 1f
            private const val INIT_ACTIVITIES_BLUE = 1f
            private const val INIT_ACTIVITIES_GREEN = 1f
            private const val INIT_ACTIVITIES_RED = 1f
            private const val INIT_BACKGROUND_ALPHA = 1f
            private const val INIT_BACKGROUND_BLUE = 0f
            private const val INIT_BACKGROUND_GREEN = 0f
            private const val INIT_BACKGROUND_RED = 0f
            private val INIT_STROKE_WIDTH = StrokeWidthType.MEDIUM

            /** Initial Type settings **/
            private const val CUSTOM_TEXT_MAXIMUM_LENGTH = 30
            private const val INIT_TYPE_CUSTOM_TEXT = ""
            private val INIT_TYPE_FONT_SELECTION = FontType.JOSEFIN_SANS
            private val INIT_TYPE_FONT_WEIGHT_SELECTION = FontWeightType.REGULAR
            private val INIT_TYPE_FONT_SIZE_SELECTION = FontSizeType.MEDIUM
            private const val INIT_TYPE_IS_ITALICIZED = false
            private val INIT_TYPE_TYPE = EditArtTypeType.NONE

            private const val NO_ACTIVITIES_COUNT = 0

            private const val INIT_SELECTION_INDEX = -1
        }

        @Inject
        @IgnoredOnParcel
        lateinit var resolutionListFactory: ResolutionListFactory

        @IgnoredOnParcel
        val atLeastOneActivitySelected
            get() = minOf(
                filterActivitiesCountDate,
                filterActivitiesCountDistance,
                filterActivitiesCountType
            ) != NO_ACTIVITIES_COUNT

        @IgnoredOnParcel
        val filterDateSelectionUnset = filterDateSelectionIndex == INIT_SELECTION_INDEX

    }
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

    val color get() = Color(red, green, blue, alpha)
    val redAsEightBit get() = red.toEightBitRepresentation()
    val greenAsEightBit get() = green.toEightBitRepresentation()
    val blueAsEightBit get() = blue.toEightBitRepresentation()
    val alphaAsEightBit get() = alpha.toEightBitRepresentation()

    private fun Float.toEightBitRepresentation(): Int {
        return (this * EIGHT_BIT_CHANNEL_LIMIT).toInt()
    }
}


sealed interface DateSelection : Parcelable {
    @Parcelize
    object All : DateSelection

    @Parcelize
    data class Year(val year: Int) : DateSelection

    @Parcelize
    data class Custom(
        @UnixMS val dateSelectedStart: Long?,
        @UnixMS val dateSelectedEnd: Long?,
        @UnixMS val dateTotalStart: Long,
        @UnixMS val dateTotalEnd: Long
    ) : DateSelection {
        val dateSelected: LongProgression?
            get() = dateSelectedEnd?.let { dateSelectedStart?.rangeTo(it) }
        val dateTotal: LongProgression
            get() = dateTotalEnd.let { dateTotalStart.rangeTo(it) }
    }
}

enum class StyleType(
    @StringRes val headerStrRes: Int,
    @StringRes val descriptionStrRes: Int
) {
    BACKGROUND(
        R.string.edit_art_style_background_header,
        R.string.edit_art_style_background_description
    ),
    ACTIVITIES(
        R.string.edit_art_style_activities_header,
        R.string.edit_art_style_activities_description
    ),
    FONT(
        R.string.edit_art_style_font_header,
        R.string.edit_art_style_font_description
    );
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


sealed interface Resolution : Parcelable {

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

    @Parcelize
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

    @Parcelize
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

    @Parcelize
    data class CustomResolution(
        override val widthPx: Int = DEFAULT_CUSTOM_WIDTH_PX,
        override val heightPx: Int = DEFAULT_CUSTOM_HEIGHT_PX,
    ) : Resolution {

        override val stringResourceId: Int
            get() = R.string.edit_art_resize_option_custom

        @Composable
        override fun displayTextResolution(): String {
            return stringResource(stringResourceId)
        }
    }
}
