package com.company.activityart.presentation.editArtScreen

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import com.company.activityart.R
import com.company.activityart.presentation.common.AppBarScaffold
import com.company.activityart.presentation.common.ScreenBackgroundLegacy
import com.company.activityart.presentation.common.type.SubheadHeavy
import com.company.activityart.presentation.editArtScreen.EditArtHeaderType.*
import com.company.activityart.presentation.editArtScreen.EditArtViewEvent.ArtMutatingEvent.ScreenMeasured
import com.company.activityart.presentation.editArtScreen.EditArtViewEvent.*
import com.company.activityart.presentation.editArtScreen.composables.EditArtDialogNavigateUp
import com.company.activityart.presentation.editArtScreen.subscreens.filters.EditArtFiltersScreen
import com.company.activityart.presentation.editArtScreen.subscreens.preview.EditArtPreview
import com.company.activityart.presentation.editArtScreen.subscreens.resize.EditArtResizeScreen
import com.company.activityart.presentation.editArtScreen.subscreens.style.EditArtStyleViewDelegate
import com.company.activityart.presentation.ui.theme.White
import com.company.activityart.presentation.ui.theme.spacing
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
                                FILTERS -> EditArtFiltersScreen(
                                    filterDateMaxDateSelectedYearMonthDay,
                                    filterDateMinDateSelectedYearMonthDay,
                                    filterDateMaxDateTotalYearMonthDay,
                                    filterDateMinDateTotalYearMonthDay,
                                    filterTypesWithSelections,
                                    scrollStateFilter,
                                    viewModel
                                )
                                STYLE -> EditArtStyleViewDelegate(
                                    styleActivities,
                                    styleBackground,
                                    scrollStateStyle,
                                    styleStrokeWidthType,
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
                } else {
                    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                        LocalDensity.current.run {
                            SideEffect {
                                viewModel.onEvent(
                                    ScreenMeasured(
                                        maxWidth.toPx().toInt(),
                                        maxHeight.toPx().toInt()
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
        EditArtDialogNavigateUp(eventReceiver = viewModel, isVisible = dialogNavigateUpActive)
    }
}