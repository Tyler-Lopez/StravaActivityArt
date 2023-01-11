package com.activityartapp.presentation.editArtScreen

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.activityartapp.R
import com.activityartapp.presentation.common.AppBarScaffold
import com.activityartapp.presentation.common.ScreenBackgroundLegacy
import com.activityartapp.presentation.common.type.SubheadHeavy
import com.activityartapp.presentation.editArtScreen.EditArtHeaderType.*
import com.activityartapp.presentation.editArtScreen.EditArtViewEvent.NavigateUpClicked
import com.activityartapp.presentation.editArtScreen.EditArtViewEvent.SaveClicked
import com.activityartapp.presentation.editArtScreen.composables.EditArtDialogNavigateUp
import com.activityartapp.presentation.editArtScreen.subscreens.filters.EditArtFiltersScreen
import com.activityartapp.presentation.editArtScreen.subscreens.preview.EditArtPreview
import com.activityartapp.presentation.editArtScreen.subscreens.resize.EditArtResizeScreen
import com.activityartapp.presentation.editArtScreen.subscreens.style.EditArtStyleViewDelegate
import com.activityartapp.presentation.editArtScreen.subscreens.type.EditArtTypeScreen
import com.activityartapp.presentation.ui.theme.White
import com.activityartapp.presentation.ui.theme.spacing
import com.activityartapp.util.classes.YearMonthDay
import com.google.accompanist.pager.ExperimentalPagerApi

/**
 * A complex screen featuring [EditArtTabLayout]
 */
@OptIn(ExperimentalPagerApi::class)
@Composable
fun EditArtViewDelegate(viewModel: EditArtViewModel) {
    viewModel.viewState.collectAsState().value?.apply {
        AppBarScaffold(
            text = stringResource(R.string.action_bar_edit_art_header),
            onNavigateUp = { viewModel.onEventDebounced(NavigateUpClicked) },
            actions = {
                IconButton(onClick = { viewModel.onEventDebounced(SaveClicked) }) {
                    SubheadHeavy(
                        text = stringResource(
                            R.string.button_continue_uppercase
                        ),
                        textColor = White
                    )
                }
                Spacer(modifier = Modifier.width(spacing.medium))
            },
            tabLayout = {
                pagerStateWrapper.apply {
                    EditArtTabLayout(
                        pagerHeaders = pagerHeaders,
                        pagerState = pagerState,
                        eventReceiver = viewModel
                    )
                }
            }
        ) {
            ScreenBackgroundLegacy {
                if (this@apply is EditArtViewState.Standby) {
                    val activeHeader =
                        EditArtHeaderType.fromOrdinal(pagerStateWrapper.pagerState.currentPage)
                    Crossfade(
                        targetState = activeHeader,
                        animationSpec = tween(500, easing = FastOutSlowInEasing)
                    ) {
                        ScreenBackgroundLegacy {
                            when (it) {
                                PREVIEW -> EditArtPreview(bitmap)
                                FILTERS -> YearMonthDay.run {
                                    EditArtFiltersScreen(
                                        filterActivitiesCountDate,
                                        filterActivitiesCountDistance,
                                        filterActivitiesCountType,
                                        filterDateSelections,
                                        filterDateSelectionIndex,
                                        filterDistanceSelected,
                                        filterDistanceTotal,
                                        filterTypes,
                                        scrollStateFilter,
                                        viewModel
                                    )
                                }
                                STYLE -> EditArtStyleViewDelegate(
                                    styleActivities,
                                    styleBackground,
                                    styleFont,
                                    scrollStateStyle,
                                    styleStrokeWidthType,
                                    viewModel
                                )
                                TYPE -> EditArtTypeScreen(
                                    typeActivitiesDistanceMetersSummed,
                                    typeAthleteName,
                                    typeCenterCustomText,
                                    typeLeftCustomText,
                                    typeRightCustomText,
                                    typeFontSelected,
                                    typeFontWeightSelected,
                                    typeFontItalicized,
                                    typeFontSizeSelected,
                                    typeMaximumCustomTextLength,
                                    typeCenterSelected,
                                    typeLeftSelected,
                                    typeRightSelected,
                                    scrollStateType,
                                    viewModel
                                )
                                RESIZE -> EditArtResizeScreen(
                                    sizeCustomHeightPx,
                                    sizeCustomWidthPx,
                                    sizeCustomRangePx,
                                    sizeResolutionList,
                                    scrollStateResize,
                                    sizeResolutionListSelectedIndex,
                                    viewModel
                                )
                                null -> error("Invalid pagerState current page.")
                            }
                        }
                    }
                }
            }
        }
        EditArtDialogNavigateUp(eventReceiver = viewModel, isVisible = dialogNavigateUpActive)
    }
}