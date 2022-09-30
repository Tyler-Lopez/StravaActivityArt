package com.company.activityart.presentation.edit_art_screen

import androidx.activity.compose.BackHandler
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.Dialog
import com.company.activityart.R
import com.company.activityart.presentation.common.AppBarScaffold
import com.company.activityart.presentation.common.ScreenBackground
import com.company.activityart.presentation.common.button.ButtonSize
import com.company.activityart.presentation.common.button.LowEmphasisButton
import com.company.activityart.presentation.common.type.Body
import com.company.activityart.presentation.common.type.SubheadHeavy
import com.company.activityart.presentation.edit_art_screen.EditArtHeaderType.*
import com.company.activityart.presentation.edit_art_screen.EditArtViewEvent.ArtMutatingEvent.*
import com.company.activityart.presentation.edit_art_screen.EditArtViewEvent.NavigateUpClicked
import com.company.activityart.presentation.edit_art_screen.composables.EditArtDialogNavigateUp
import com.company.activityart.presentation.edit_art_screen.subscreens.filters.EditArtFiltersScreen
import com.company.activityart.presentation.edit_art_screen.subscreens.preview.EditArtPreview
import com.company.activityart.presentation.edit_art_screen.subscreens.resize.EditArtResizeScreen
import com.company.activityart.presentation.edit_art_screen.subscreens.style.EditArtStyleViewDelegate
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
                IconButton(onClick = { }) {
                    SubheadHeavy(
                        text = stringResource(
                            R.string.button_save_uppercase
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
            ScreenBackground {
                if (this@apply is EditArtViewState.Standby) {
                    val activeHeader =
                        EditArtHeaderType.fromOrdinal(pagerStateWrapper.pagerState.currentPage)
                    Crossfade(
                        targetState = activeHeader,
                        animationSpec = tween(500, easing = FastOutSlowInEasing)
                    ) {
                        ScreenBackground {
                            when (it) {
                                PREVIEW -> EditArtPreview(bitmap)
                                FILTERS -> EditArtFiltersScreen(
                                    filterDateMaxDateSelectedYearMonthDay,
                                    filterDateMinDateSelectedYearMonthDay,
                                    filterDateMaxDateTotalYearMonthDay,
                                    filterDateMinDateTotalYearMonthDay,
                                    scrollStateFilter,
                                    viewModel
                                )
                                //EditArtFiltersViewDelegate(filterViewModel)
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
        EditArtDialogNavigateUp(eventReceiver = viewModel, isVisible = dialogNavigateUpActive)
    }
}