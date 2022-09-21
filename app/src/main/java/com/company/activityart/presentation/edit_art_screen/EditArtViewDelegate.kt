package com.company.activityart.presentation.edit_art_screen

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.company.activityart.R
import com.company.activityart.presentation.common.AppBarScaffold
import com.company.activityart.presentation.common.ScreenBackground
import com.company.activityart.presentation.common.type.SubheadHeavy
import com.company.activityart.presentation.edit_art_screen.EditArtHeaderType.*
import com.company.activityart.presentation.edit_art_screen.EditArtViewEvent.NavigateUpClicked
import com.company.activityart.presentation.edit_art_screen.subscreens.filters.EditArtFiltersViewDelegate
import com.company.activityart.presentation.edit_art_screen.subscreens.filters.EditArtFiltersViewModel
import com.company.activityart.presentation.edit_art_screen.subscreens.preview.EditArtPreviewViewDelegate
import com.company.activityart.presentation.edit_art_screen.subscreens.preview.EditArtPreviewViewModel
import com.company.activityart.presentation.edit_art_screen.subscreens.resize.EditArtResize
import com.company.activityart.presentation.edit_art_screen.subscreens.style.EditArtStyle
import com.company.activityart.presentation.edit_art_screen.subscreens.type.EditArtType
import com.company.activityart.presentation.ui.theme.White
import com.company.activityart.presentation.ui.theme.spacing
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import kotlinx.coroutines.launch

/**
 * A complex screen featuring [EditArtTabLayout]
 */
@OptIn(ExperimentalPagerApi::class)
@Composable
fun EditArtViewDelegate(viewModel: EditArtViewModel) {
    viewModel.viewState.collectAsState().value?.apply {
        /** Updates pagerState to new position if necessary **/
        pagerStateWrapper.apply {
            LaunchedEffect(pagerNewPosition) {
                if (pagerNewPosition != pagerState.currentPage)
                    pagerState.scrollToPage(pagerNewPosition)
            }
        }
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
                    when (EditArtHeaderType.fromOrdinal(pagerStateWrapper.pagerState.currentPage)) {
                        PREVIEW -> EditArtPreviewViewDelegate(
                            filterStateWrapper,
                            sizeWrapper,
                            hiltViewModel<EditArtPreviewViewModel>().apply {
                                attachParent(viewModel)
                            },
                        )
                        FILTERS -> EditArtFiltersViewDelegate(
                            hiltViewModel<EditArtFiltersViewModel>().apply {
                                attachParent(viewModel)
                            },
                        )
                        STYLE -> EditArtStyle()
                        TYPE -> EditArtType()
                        RESIZE -> EditArtResize()
                        null -> error("Invalid pagerState current page.")
                    }
                }
            }
        }
    }
}