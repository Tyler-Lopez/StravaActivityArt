package com.company.activityart.presentation.edit_art_screen

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import com.company.activityart.R
import com.company.activityart.presentation.common.AppBarScaffold
import com.company.activityart.presentation.common.ScreenBackground
import com.company.activityart.presentation.common.type.SubheadHeavy
import com.company.activityart.presentation.edit_art_screen.EditArtHeaderType.*
import com.company.activityart.presentation.edit_art_screen.EditArtViewEvent.ArtMutatingEvent.*
import com.company.activityart.presentation.edit_art_screen.EditArtViewEvent.NavigateUpClicked
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
                                //  FILTERS -> EditArtFiltersViewDelegate(filterViewModel)
                                STYLE -> EditArtStyleViewDelegate(
                                    styleActivities,
                                    styleBackground,
                                    styleStrokeWidthType,
                                    viewModel
                                )
                                RESIZE -> EditArtResizeScreen(sizeActual, viewModel)
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
    }
}