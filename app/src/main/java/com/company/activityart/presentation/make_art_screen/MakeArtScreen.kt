package com.company.activityart.presentation.make_art_screen

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.company.activityart.R
import com.company.activityart.presentation.common.AppBarScaffold
import com.company.activityart.presentation.common.ScreenBackground
import com.company.activityart.presentation.common.type.Body
import com.company.activityart.presentation.common.type.Subhead
import com.company.activityart.presentation.common.type.SubheadHeavy
import com.company.activityart.presentation.common.type.TitleFour
import com.company.activityart.presentation.make_art_screen.MakeArtViewEvent.*
import com.company.activityart.presentation.make_art_screen.MakeArtViewEvent.NavigateUpClicked
import com.company.activityart.presentation.make_art_screen.MakeArtViewState.Loading
import com.company.activityart.presentation.make_art_screen.MakeArtViewState.Standby
import com.company.activityart.presentation.make_art_screen.composables.MakeArtStandby
import com.company.activityart.presentation.ui.theme.Rust
import com.company.activityart.presentation.ui.theme.StravaOrange
import com.company.activityart.presentation.ui.theme.White
import com.company.activityart.presentation.ui.theme.spacing
import com.google.accompanist.pager.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun MakeArtScreen(viewModel: MakeArtViewModel) {
    val coroutineScope = rememberCoroutineScope()
    viewModel.viewState.collectAsState().value?.apply {
        /** Update pagerState to new position if necessary **/
        coroutineScope.launch { pagerState.animateScrollToPage(newPosition) }
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
                MakeArtTabLayout(
                    pagerHeaders = pageHeaders,
                    pagerState = pagerState,
                    eventReceiver = viewModel
                )
            }
        ) {

        }
    }
}

// on below line we are creating a tab content method
// in which we will be displaying the individual page of our tab .
@ExperimentalPagerApi
@Composable
fun TabsContent(pagerState: PagerState) {
    // on below line we are creating
    // horizontal pager for our tab layout.
    HorizontalPager(state = pagerState) {
        // on below line we are specifying
        // the different pages.
            page ->
        when (page) {
            // on below line we are calling tab content screen
            // and specifying data as Home Screen.
            0 -> Text("Hello 1")
            // on below line we are calling tab content screen
            // and specifying data as Shopping Screen.
            1 -> Text("Hello 2")
            // on below line we are calling tab content screen
            // and specifying data as Settings Screen.
            2 -> Text("Hello 3")
            3 -> Text("Hello 4")
        }
    }
}