package com.activityartapp.util

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed interface NavArgSpecification {

    companion object {
        private const val ACTIVITY_COLOR_RULES = "activityColorRules"
        private const val ACTIVITY_TYPES_KEY = "activityTypes"
        private const val ATHLETE_ID_KEY = "athleteId"
        private const val BACKGROUND_GRADIENT_ANGLE_TYPE_KEY = "backgroundAngleType"
        private const val BACKGROUND_TYPE_KEY = "backgroundType"
        private const val COLOR_ACTIVITIES_KEY = "colorActivities"
        private const val COLORS_BACKGROUND_KEY = "colorsBackground"
        private const val COLOR_FONT_KEY = "colorFont"
        private const val ERROR_SCREEN_TYPE = "errorScreenType"
        private const val FILTER_DATE_AFTER_MS_KEY = "filterDateAfterMs"
        private const val FILTER_DATE_BEFORE_MS_KEY = "filterDateBeforeMs"
        private const val FILTER_DISTANCE_LESS_THAN_KEY = "filterDistanceLessThanMeters"
        private const val FILTER_DISTANCE_MORE_THAN_KEY = "filterDistanceMoreThanMeters"
        private const val SIZE_HEIGHT_KEY = "sizeHeight"
        private const val SIZE_WIDTH_KEY = "sizeWidth"
        private const val SORT_TYPE_KEY = "sortType"
        private const val SORT_DIRECTION_TYPE_KEY = "sortDirectionType"
        private const val STROKE_WIDTH_KEY = "strokeWidth"
        private const val TEXT_LEFT_KEY = "textLeft"
        private const val TEXT_CENTER_KEY = "textCenter"
        private const val TEXT_RIGHT_KEY = "textRight"
        private const val TEXT_FONT_ASSET_PATH_KEY = "textFontAssetPath"
        private const val TEXT_FONT_SIZE_KEY = "textFontSize"

        private const val ERROR_MISSING_KEY = "ERROR: Missing key [%s] in saved state handle."
    }

    val name: String

    val key: String
        get() = navArg.name

    val navArg: NamedNavArgument
        get() = navArgument(name = name) { type = NavType.StringType }

    val route: String
        get() = "$key={$key}"

    fun rawArg(handle: SavedStateHandle): String {
        return handle[key] ?: error(ERROR_MISSING_KEY.format(key))
    }

    object ActivityColorRulesArg : NavArgSpecification {
        override val name: String = ACTIVITY_COLOR_RULES
    }

    object ActivityTypesArg : NavArgSpecification {
        override val name = ACTIVITY_TYPES_KEY
    }

    object AthleteIdArg : NavArgSpecification {
        override val name = ATHLETE_ID_KEY
    }

    object BackgroundTypeArg : NavArgSpecification {
        override val name = BACKGROUND_TYPE_KEY
    }

    object BackgroundGradientAngleTypeArg : NavArgSpecification {
        override val name = BACKGROUND_GRADIENT_ANGLE_TYPE_KEY
    }

    object ColorActivitiesArgbArg : NavArgSpecification {
        override val name = COLOR_ACTIVITIES_KEY
    }

    object ColorBackgroundArgbListArg : NavArgSpecification {
        override val name = COLORS_BACKGROUND_KEY
    }

    object ColorFontArgbArg : NavArgSpecification {
        override val name: String = COLOR_FONT_KEY
    }

    object ErrorScreenArg : NavArgSpecification {
        override val name: String = ERROR_SCREEN_TYPE
    }

    object FilterDateAfterMsArg : NavArgSpecification {
        override val name = FILTER_DATE_AFTER_MS_KEY
    }

    object FilterDateBeforeMsArg : NavArgSpecification {
        override val name = FILTER_DATE_BEFORE_MS_KEY
    }

    object FilterDistanceLessThanMetersArg : NavArgSpecification {
        override val name: String = FILTER_DISTANCE_LESS_THAN_KEY
    }

    object FilterDistanceMoreThanMetersArg : NavArgSpecification {
        override val name: String = FILTER_DISTANCE_MORE_THAN_KEY
    }

    object StrokeWidthArg : NavArgSpecification {
        override val name = STROKE_WIDTH_KEY
    }

    object SizeHeightPxArg : NavArgSpecification {
        override val name = SIZE_HEIGHT_KEY
    }

    object SizeWidthPxArg : NavArgSpecification {
        override val name = SIZE_WIDTH_KEY
    }

    object SortTypeArg : NavArgSpecification {
        override val name: String = SORT_TYPE_KEY
    }

    object SortDirectionTypeArg : NavArgSpecification {
        override val name: String = SORT_DIRECTION_TYPE_KEY
    }

    object TextLeftArg : NavArgSpecification {
        override val name: String = TEXT_LEFT_KEY
    }

    object TextCenterArg : NavArgSpecification {
        override val name: String = TEXT_CENTER_KEY
    }

    object TextRightArg : NavArgSpecification {
        override val name: String = TEXT_RIGHT_KEY
    }

    object TextFontAssetPathArg : NavArgSpecification {
        override val name: String = TEXT_FONT_ASSET_PATH_KEY
    }

    object TextFontSizeArg : NavArgSpecification {
        override val name: String = TEXT_FONT_SIZE_KEY
    }
}

