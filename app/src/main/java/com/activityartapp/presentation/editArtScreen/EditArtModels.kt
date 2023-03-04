@file:OptIn(ExperimentalPagerApi::class)

package com.activityartapp.presentation.editArtScreen

import android.graphics.Bitmap
import android.os.Parcelable
import android.util.Size
import androidx.annotation.StringRes
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotMutableState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.graphics.*
import androidx.compose.ui.res.stringResource
import com.activityartapp.R
import com.activityartapp.architecture.ViewEvent
import com.activityartapp.architecture.ViewState
import com.activityartapp.domain.models.ResolutionListFactory
import com.activityartapp.presentation.editArtScreen.subscreens.resize.ResolutionListFactoryImpl
import com.activityartapp.presentation.editArtScreen.subscreens.type.EditArtTypeSection
import com.activityartapp.presentation.editArtScreen.subscreens.type.EditArtTypeType
import com.activityartapp.util.enums.*
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
import javax.inject.Inject
import kotlin.math.roundToInt

annotation class UnixMS

sealed interface EditArtViewEvent : ViewEvent {

    data class ClickedRemoveGradientColor(val removedIndex: Int) : EditArtViewEvent
    object ClickedInfoCheckeredBackground : EditArtViewEvent
    object ClickedInfoGradientBackground : EditArtViewEvent
    object ClickedInfoTransparentBackground : EditArtViewEvent
    object DialogDismissed : EditArtViewEvent
    object DialogNavigateUpConfirmed : EditArtViewEvent
    sealed interface FilterDistancePendingChange : EditArtViewEvent {
        val changedTo: String

        data class FilterDistancePendingChangeShortest(override val changedTo: String) :
            FilterDistancePendingChange

        data class FilterDistancePendingChangeLongest(override val changedTo: String) :
            FilterDistancePendingChange
    }

    object NavigateUpClicked : EditArtViewEvent
    data class PageHeaderClicked(val position: Int) : EditArtViewEvent
    object SaveClicked : EditArtViewEvent
    data class ScreenMeasured(val size: Size) : EditArtViewEvent
    sealed interface SizeCustomPendingChanged : EditArtViewEvent {
        val changedTo: String

        data class HeightChanged(override val changedTo: String) : SizeCustomPendingChanged
        data class WidthChanged(override val changedTo: String) : SizeCustomPendingChanged
    }

    data class StyleColorPendingChanged(
        val style: StyleIdentifier,
        val colorType: ColorType,
        val changedTo: String
    ) : EditArtViewEvent

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

            sealed interface FilterDistancePendingChangeConfirmed : FilterChanged {
                object StartConfirmed : FilterDistancePendingChangeConfirmed {
                    override val filterType: EditArtFilterType
                        get() = EditArtFilterType.DISTANCE
                }

                object EndConfirmed : FilterDistancePendingChangeConfirmed {
                    override val filterType: EditArtFilterType
                        get() = EditArtFilterType.DISTANCE
                }
            }

            data class FilterTypeToggled(val type: SportType) : FilterChanged {
                override val filterType: EditArtFilterType
                    get() = EditArtFilterType.TYPE
            }
        }

        data class SizeChanged(val changedIndex: Int) : ArtMutatingEvent
        data class SizeCustomChanged(
            val customIndex: Int,
            val changedToPx: Int,
            val heightChanged: Boolean
        ) : ArtMutatingEvent

        data class SizeCustomPendingChangeConfirmed(val customIndex: Int) : ArtMutatingEvent
        data class SizeRotated(val rotatedIndex: Int) : ArtMutatingEvent
        data class SortDirectionChanged(val changedTo: EditArtSortDirectionType) : ArtMutatingEvent
        data class SortTypeChanged(val changedTo: EditArtSortType) : ArtMutatingEvent
        data class StyleGradientAngleTypeChanged(val changedTo: AngleType) : ArtMutatingEvent
        data class StyleBackgroundTypeChanged(val changedTo: BackgroundType) : ArtMutatingEvent
        object StyleBackgroundColorAdded : ArtMutatingEvent
        object StyleBackgroundColorRemoveConfirmed : ArtMutatingEvent
        data class StyleColorChanged(
            val colorType: ColorType,
            val changedTo: Float,
            val style: StyleIdentifier
        ) : ArtMutatingEvent

        data class StyleColorPendingChangeConfirmed(val style: StyleIdentifier) : ArtMutatingEvent
        data class StyleColorFontUseCustomChanged(val useCustom: Boolean) : ArtMutatingEvent
        data class StyleStrokeWidthChanged(val changedTo: StrokeWidthType) : ArtMutatingEvent
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
        const val MAX_GRADIENT_BG_COLORS = 7
        const val MIN_GRADIENT_BG_COLORS = 2
    }

    val dialogActive: State<EditArtDialog>
    val pagerStateWrapper: PagerStateWrapper

    data class Loading(
        override val dialogActive: State<EditArtDialog>,
        override val pagerStateWrapper: PagerStateWrapper
    ) : EditArtViewState

    data class Standby(
        val bitmap: State<Bitmap?>,
        override val dialogActive: State<EditArtDialog>,
        val filterActivitiesCountDate: State<Int>,
        val filterActivitiesCountDistance: State<Int>,
        val filterActivitiesCountType: State<Int>,
        val filterDateSelections: SnapshotStateList<DateSelection>,
        val filterDateSelectionIndex: State<Int>,
        val filterDistanceSelectedStart: State<Double?>,
        val filterDistanceSelectedEnd: State<Double?>,
        val filterDistanceTotalStart: State<Double?>,
        val filterDistanceTotalEnd: State<Double?>,
        val filterDistancePendingChangeStart: State<String?>,
        val filterDistancePendingChangeEnd: State<String?>,
        val filterTypes: SnapshotStateList<Pair<SportType, Boolean>>,
        override val pagerStateWrapper: PagerStateWrapper,
        val listStateFilter: LazyListState = LazyListState(),
        val listStateStyle: LazyListState = LazyListState(),
        val scrollStateType: ScrollState = ScrollState(INITIAL_SCROLL_STATE),
        val scrollStateResize: ScrollState = ScrollState(INITIAL_SCROLL_STATE),
        val scrollStateSort: ScrollState = ScrollState(INITIAL_SCROLL_STATE),
        val sizeResolutionList: SnapshotStateList<Resolution>,
        val sizeResolutionListSelectedIndex: State<Int>,
        val sortTypeSelected: State<EditArtSortType>,
        val sortDirectionTypeSelected: State<EditArtSortDirectionType>,
        val styleActivities: State<ColorWrapper>,
        val styleBackgroundList: SnapshotStateList<ColorWrapper>,
        val styleBackgroundAngleType: State<AngleType>,
        val styleBackgroundGradientColorCount: State<Int>,
        val styleBackgroundType: State<BackgroundType>,
        val styleFont: State<ColorWrapper?>,
        val styleStrokeWidthType: State<StrokeWidthType>,
        val typeActivitiesDistanceMetersSummed: State<Int>,
        val typeFontSelected: State<FontType>,
        val typeFontWeightSelected: State<FontWeightType>,
        val typeFontItalicized: State<Boolean>,
        val typeFontSizeSelected: State<FontSizeType>,
        val typeMaximumCustomTextLength: Int,
        val typeLeftSelected: State<EditArtTypeType>,
        val typeLeftCustomText: State<String>,
        val typeCenterSelected: State<EditArtTypeType>,
        val typeCenterCustomText: State<String>,
        val typeRightSelected: State<EditArtTypeType>,
        val typeRightCustomText: State<String>,
    ) : EditArtViewState {

        companion object {
            private const val INITIAL_SCROLL_STATE = 0
        }

        @Inject
        lateinit var resolutionListFactory: ResolutionListFactory


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
// https://developer.android.com/reference/kotlin/androidx/compose/runtime/Immutable
@Immutable
data class ColorWrapper(
    val alpha: Float,
    val blue: Float,
    val green: Float,
    val red: Float,
    @IgnoredOnParcel val pendingAlpha: String? = null,
    @IgnoredOnParcel val pendingBlue: String? = null,
    @IgnoredOnParcel val pendingGreen: String? = null,
    @IgnoredOnParcel val pendingRed: String? = null
) : Parcelable {

    companion object {
        private const val RATIO_LIMIT_LOWER = 0f
        private const val RATIO_LIMIT_UPPER = 1f
        private const val EIGHT_BIT_LIMIT_LOWER = 0
        private const val EIGHT_BIT_LIMIT_UPPER = 255

        fun ratioToEightBit(ratio: Float): Int {
            return (ratio * EIGHT_BIT_LIMIT_UPPER).toInt()
        }

        fun eightBitToRatio(eightBit: Int): Float {
            return (eightBit / EIGHT_BIT_LIMIT_UPPER.toFloat())
        }

        val RATIO_RANGE = RATIO_LIMIT_LOWER..RATIO_LIMIT_UPPER
        val EIGHT_BIT_RANGE = EIGHT_BIT_LIMIT_LOWER..EIGHT_BIT_LIMIT_UPPER

        val Black = ColorWrapper(
            alpha = 1f,
            blue = 0f,
            green = 0f,
            red = 0f
        )

        val White = ColorWrapper(
            alpha = 1f,
            blue = 1f,
            green = 1f,
            red = 1f
        )
    }

    fun toInvertedLuminanceGrayscaleColor() = (RATIO_LIMIT_UPPER - toColor().luminance()).let {
        Color(it, it, it)
    }

    fun toColor() = Color(red, green, blue, alpha)
    fun toColorArgb() = toColor().toArgb()
    fun redToEightBitString() = ratioToEightBit(red).toString()
    fun greenToEightBitString() = ratioToEightBit(green).toString()
    fun blueToEightBitString() = ratioToEightBit(blue).toString()
    fun alphaToEightBitString() = ratioToEightBit(alpha).toString()
}

@Stable
sealed interface DateSelection : Parcelable {
    @Parcelize
    @Stable
    object All : DateSelection

    @Parcelize
    @Stable
    data class Year(val year: Int) : DateSelection

    @Parcelize
    @Stable
    data class Custom(
        val dateSelectedStartUnixMs: Long,
        val dateSelectedEndUnixMs: Long,
        val dateTotalStartUnixMs: Long,
        val dateTotalEndUnixMs: Long
    ) : DateSelection {
        @IgnoredOnParcel
        val dateSelected: LongProgression
            get() = dateSelectedStartUnixMs..dateSelectedEndUnixMs

        @IgnoredOnParcel
        val dateTotal: LongProgression
            get() = dateTotalStartUnixMs..dateTotalEndUnixMs
    }
}

sealed interface StyleIdentifier {
    object Activities : StyleIdentifier
    data class Background(val index: Int) : StyleIdentifier
    object Font : StyleIdentifier
}

enum class ColorType(@StringRes val strRes: Int) {
    RED(R.string.edit_art_style_color_red),
    GREEN(R.string.edit_art_style_color_green),
    BLUE(R.string.edit_art_style_color_blue),
    ALPHA(R.string.edit_art_style_color_alpha)
}

enum class StrokeWidthType(val headerId: Int) {
    THIN(R.string.edit_art_style_stroke_thin),
    MEDIUM(R.string.edit_art_style_stroke_medium),
    THICK(R.string.edit_art_style_stroke_thick);
}

@Stable
sealed interface Resolution : Parcelable {

    val widthPx: Int
    val heightPx: Int
    val stringResourceId: Int

    @Composable
    fun displayTextResolution(): String

    @Stable
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
    @Stable
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
    @Stable
    data class CustomResolution(
        override val widthPx: Int,
        override val heightPx: Int,
        private val sizeMaximumPx: Int,
        private val sizeMinimumPx: Int,
        @IgnoredOnParcel val pendingWidth: String? = null,
        @IgnoredOnParcel val pendingHeight: String? = null
    ) : Resolution {

        @IgnoredOnParcel
        override val stringResourceId: Int
            get() = R.string.edit_art_resize_option_custom

        @IgnoredOnParcel
        val sizeRangePx: IntRange = sizeMinimumPx..sizeMaximumPx

        @Composable
        override fun displayTextResolution(): String {
            return stringResource(stringResourceId)
        }
    }
}
