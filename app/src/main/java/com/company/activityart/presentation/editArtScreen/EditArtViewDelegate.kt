package com.company.activityart.presentation.editArtScreen

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
import com.company.activityart.R
import com.company.activityart.presentation.common.AppBarScaffold
import com.company.activityart.presentation.common.ScreenBackgroundLegacy
import com.company.activityart.presentation.common.type.SubheadHeavy
import com.company.activityart.presentation.editArtScreen.EditArtHeaderType.*
import com.company.activityart.presentation.editArtScreen.EditArtViewEvent.NavigateUpClicked
import com.company.activityart.presentation.editArtScreen.EditArtViewEvent.SaveClicked
import com.company.activityart.presentation.editArtScreen.composables.EditArtDialogNavigateUp
import com.company.activityart.presentation.editArtScreen.subscreens.filters.EditArtFiltersScreen
import com.company.activityart.presentation.editArtScreen.subscreens.preview.EditArtPreview
import com.company.activityart.presentation.editArtScreen.subscreens.resize.EditArtResizeScreen
import com.company.activityart.presentation.editArtScreen.subscreens.style.EditArtStyleViewDelegate
import com.company.activityart.presentation.editArtScreen.subscreens.type.EditArtTypeScreen
import com.company.activityart.presentation.ui.theme.White
import com.company.activityart.presentation.ui.theme.spacing
import com.company.activityart.util.classes.YearMonthDay
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
                                        filterDateSelected?.last?.let { fromUnixMs(it) },
                                        filterDateSelected?.first?.let { fromUnixMs(it) },
                                        filterDateTotal?.last?.let { fromUnixMs(it) },
                                        filterDateTotal?.first?.let { fromUnixMs(it) },
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
                                    scrollStateStyle,
                                    styleStrokeWidthType,
                                    viewModel
                                )
                                TYPE -> EditArtTypeScreen(
                                    typeActivitiesDistanceMetersSummed,
                                    typeCenterCustomText,
                                    typeLeftCustomText,
                                    typeRightCustomText,
                                    typeMaximumCustomTextLength,
                                    typeCenterSelected,
                                    typeLeftSelected,
                                    typeRightSelected,
                                    scrollStateStyle,
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