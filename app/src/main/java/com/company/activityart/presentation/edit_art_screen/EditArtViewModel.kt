package com.company.activityart.presentation.edit_art_screen

import android.util.Size
import androidx.lifecycle.viewModelScope
import com.company.activityart.architecture.BaseRoutingViewModel
import com.company.activityart.domain.use_case.activities.GetActivitiesFromCacheUseCase
import com.company.activityart.presentation.MainDestination
import com.company.activityart.presentation.MainDestination.NavigateUp
import com.company.activityart.presentation.edit_art_screen.EditArtViewEvent.*
import com.company.activityart.presentation.edit_art_screen.ColorType.*
import com.company.activityart.presentation.edit_art_screen.StyleType.*
import com.company.activityart.presentation.edit_art_screen.EditArtViewEvent.FilterTypeChanged.FilterTypeAdded
import com.company.activityart.presentation.edit_art_screen.EditArtViewState.Loading
import com.company.activityart.presentation.edit_art_screen.EditArtViewState.Standby
import com.company.activityart.util.TimeUtils
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalPagerApi::class)
@HiltViewModel
class EditArtViewModel @Inject constructor(
    private val activitiesFromCacheUseCase: GetActivitiesFromCacheUseCase,
    private val timeUtils: TimeUtils
) : BaseRoutingViewModel<EditArtViewState, EditArtViewEvent, MainDestination>() {

    companion object {
        private const val FADE_LENGTH_MS = 1000
        private const val INITIAL_ACTIVITIES_ALPHA = 1f
        private const val INITIAL_ACTIVITIES_BLUE = 1f
        private const val INITIAL_ACTIVITIES_GREEN = 1f
        private const val INITIAL_ACTIVITIES_RED = 1f
        private const val INITIAL_BACKGROUND_ALPHA = 1f
        private const val INITIAL_BACKGROUND_BLUE = 0f
        private const val INITIAL_BACKGROUND_GREEN = 0f
        private const val INITIAL_BACKGROUND_RED = 0f
        private const val INITIAL_HEIGHT_PX = 1080
        private const val INITIAL_WIDTH_PX = 1920
    }

    init {
        /** On the Main thread, push a state containing the header asap **/
        val pagerHeaders: List<EditArtHeaderType> = EditArtHeaderType.values().toList()
        val pagerState = PagerState(pagerHeaders.size)
        val pagerStateWrapper = PagerStateWrapper(
            pagerHeaders = pagerHeaders,
            pagerState = pagerState,
            fadeLengthMs = FADE_LENGTH_MS
        )
        pushState(Loading(pagerStateWrapper = pagerStateWrapper))

        /** Compute state initializations **/
        viewModelScope.launch(Dispatchers.Default) {
            val activities = activitiesFromCacheUseCase().flatMap { it.value }
            val activitiesUnixSeconds =
                activities.map { timeUtils.iso8601StringToUnixSecond(it.iso8601LocalDate) }

            val unixSecondFirst = activitiesUnixSeconds.min()
            val unixSecondLast = activitiesUnixSeconds.max()

            pushState(
                Standby(
                    filterStateWrapper = FilterStateWrapper(
                        unixSecondSelectedStart = unixSecondFirst,
                        unixSecondSelectedEnd = unixSecondLast
                    ),
                    pagerStateWrapper = pagerStateWrapper,
                    size = Size(
                        INITIAL_WIDTH_PX,
                        INITIAL_HEIGHT_PX
                    ),
                    styleActivities = ColorWrapper(
                        INITIAL_ACTIVITIES_ALPHA,
                        INITIAL_ACTIVITIES_BLUE,
                        INITIAL_ACTIVITIES_GREEN,
                        INITIAL_ACTIVITIES_RED
                    ),
                    styleBackground = ColorWrapper(
                        INITIAL_BACKGROUND_ALPHA,
                        INITIAL_BACKGROUND_BLUE,
                        INITIAL_BACKGROUND_GREEN,
                        INITIAL_BACKGROUND_RED
                    )
                )
            )
        }
    }

    override fun onEvent(event: EditArtViewEvent) {
        when (event) {
            is FilterDateChanged -> onFilterDateChanged(event)
            is FilterTypeChanged -> onFilterTypeChanged(event)
            is MakeFullscreenClicked -> onMakeFullscreenClicked()
            is NavigateUpClicked -> onNavigateUpClicked()
            is PageHeaderClicked -> onPageHeaderClicked(event)
            is SaveClicked -> onSaveClicked()
            is SelectFiltersClicked -> onSelectFiltersClicked()
            is SelectStylesClicked -> onSelectStylesClicked()
            is StylesColorChanged -> onStylesColorChanged(event)
        }
    }

    private fun onFilterDateChanged(event: FilterDateChanged) {
        (lastPushedState as? Standby)?.run {
            copy(
                filterStateWrapper = filterStateWrapper.copy(
                    unixSecondSelectedStart = event.newUnixSecondStart,
                    unixSecondSelectedEnd = event.newUnixSecondEnd
                )
            )
        }?.push()
    }

    private fun onFilterTypeChanged(event: FilterTypeChanged) {
        (lastPushedState as? Standby)?.run {
            copy(
                filterStateWrapper = filterStateWrapper.copy(
                    excludedActivityTypes = filterStateWrapper
                        .excludedActivityTypes
                        .toMutableSet()
                        .apply {
                            if (event is FilterTypeAdded) {
                                add(event.type)
                            } else {
                                remove(event.type)
                            }
                        }
                )
            )
        }?.push()
    }

    private fun onMakeFullscreenClicked() {

    }

    private fun onNavigateUpClicked() {
        viewModelScope.launch(Dispatchers.Default) {
            routeTo(NavigateUp)
        }
    }

    private fun onPageHeaderClicked(event: PageHeaderClicked) {
        viewModelScope.launch(Dispatchers.Main.immediate) {
            (lastPushedState as? Standby)?.run {
                pagerStateWrapper.pagerState.scrollToPage(event.position)
            }
        }
    }

    private fun onSaveClicked() {

    }

    private fun onSelectFiltersClicked() {

    }

    private fun onSelectStylesClicked() {

    }

    private fun onStylesColorChanged(event: StylesColorChanged) {
        (lastPushedState as? Standby)?.run {
            event.run {
                when (styleType) {
                    BACKGROUND -> copy(styleBackground = styleBackground.copyWithEvent(event))
                    ACTIVITIES -> copy(styleActivities = styleActivities.copyWithEvent(event))
                }
            }
        }?.push()
    }

    /** @return Copy of [ColorWrapper] which reflects a change in blue, green, or red values. **/
    private fun ColorWrapper.copyWithEvent(event: StylesColorChanged): ColorWrapper {
        return event.let {
            when (it.colorType) {
                ALPHA -> copy(alpha = it.changedTo)
                BLUE -> copy(blue = it.changedTo)
                GREEN -> copy(green = it.changedTo)
                RED -> copy(red = it.changedTo)
            }
        }
    }
}