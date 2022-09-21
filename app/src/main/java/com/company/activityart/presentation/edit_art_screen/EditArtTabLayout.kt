package com.company.activityart.presentation.edit_art_screen

import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.company.activityart.architecture.EventReceiver
import com.company.activityart.presentation.edit_art_screen.EditArtViewEvent.*
import com.company.activityart.presentation.ui.theme.Rust
import com.company.activityart.presentation.ui.theme.White
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.pagerTabIndicatorOffset

@OptIn(ExperimentalPagerApi::class)
@Composable
fun EditArtTabLayout(
    pagerHeaders: List<EditArtHeaderType>,
    pagerState: PagerState,
    eventReceiver: EventReceiver<EditArtViewEvent>
) {
    // on below line we are creating
    // a variable for the scope.
//    val scope = rememberCoroutineScope()
    // on below line we are creating a
    // individual row for our tab layout.
    ScrollableTabRow(
        // on below line we are specifying
        // the selected index.
        selectedTabIndex = pagerState.currentPage,

        // on below line we are
        // specifying background color.
        backgroundColor = Rust,

        // on below line we are specifying content color.
        contentColor = Color.White,

        // on below line we are specifying
        // the indicator for the tab
        indicator = { tabPositions ->
            // on below line we are specifying the styling
            // for tab indicator by specifying height
            // and color for the tab indicator.
            TabRowDefaults.Indicator(
                Modifier.pagerTabIndicatorOffset(pagerState, tabPositions),
                height = 2.dp,
                color = Color.White
            )
        }
    ) {
        // on below line we are specifying icon
        // and text for the individual tab item
        pagerHeaders.forEachIndexed { index, header ->
            Tab(
                selected = pagerState.currentPage == index,
                onClick = {
                    // on below line we are specifying the scope.
                    eventReceiver.onEvent(PageHeaderClicked(position = index))
                },
                text = {
                    Text(
                        text = stringResource(header.textStr),
                        style = MaterialTheme.typography.subtitle2,
                    )
                },
                icon = {
                    Icon(
                        imageVector = header.icon,
                        contentDescription = stringResource(header.contentDescription)
                    )
                },
                unselectedContentColor = Color(1f, 1f, 1f, .5f),
                selectedContentColor = White
            )
        }
    }
}