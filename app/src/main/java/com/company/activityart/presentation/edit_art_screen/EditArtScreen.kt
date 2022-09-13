package com.company.activityart.presentation.edit_art_screen

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.company.activityart.R
import com.company.activityart.presentation.common.AppBarScaffold
import com.company.activityart.presentation.common.ScreenBackground
import com.company.activityart.presentation.common.type.SubheadHeavy
import com.company.activityart.presentation.edit_art_screen.EditArtHeaderType.*
import com.company.activityart.presentation.edit_art_screen.EditArtViewEvent.NavigateUpClicked
import com.company.activityart.presentation.edit_art_screen.subscreens.filters.EditArtFilters
import com.company.activityart.presentation.edit_art_screen.subscreens.preview.EditArtPreview
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
fun EditArtScreen(viewModel: EditArtViewModel) {
    val coroutineScope = rememberCoroutineScope()
    viewModel.viewState.collectAsState().value?.apply {
        /** Updates pagerState to new position if necessary **/
        coroutineScope.launch {
            pagerStateWrapper.pagerState.animateScrollToPage(
                pagerStateWrapper.pagerNewPosition
            )
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
                EditArtTabLayout(
                    pagerHeaders = pagerStateWrapper.pagerHeaders,
                    pagerState = pagerStateWrapper.pagerState,
                    eventReceiver = viewModel
                )
            }
        ) {
            HorizontalPager(
                state = pagerStateWrapper.pagerState,

                /* TODO
                This is disabled due to wanting to intercept drag event in view model
                rather than having it be controlled entirely by the view. See in future
                if control that event in view model layer. */
                dragEnabled = false
            ) { page ->
                ScreenBackground {
                    when (EditArtHeaderType.fromOrdinal(page)) {
                        PREVIEW -> EditArtPreview()
                        FILTERS -> EditArtFilters(filterStateWrapper, viewModel)
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