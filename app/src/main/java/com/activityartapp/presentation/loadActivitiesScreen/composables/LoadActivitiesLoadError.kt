package com.activityartapp.presentation.loadActivitiesScreen.composables

import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.activityartapp.R
import com.activityartapp.architecture.EventReceiver
import com.activityartapp.presentation.common.button.ButtonSize.*
import com.activityartapp.presentation.common.button.HighEmphasisButton
import com.activityartapp.presentation.loadActivitiesScreen.LoadActivitiesViewEvent
import com.activityartapp.presentation.loadActivitiesScreen.LoadActivitiesViewEvent.*

/**
 * Shown when some activities or no activities have loaded.
 * Prompts athlete to either continue (if [activitiesLoaded] > 0)
 * or to try again to load.
 */
@Composable
fun LoadActivitiesLoadError(
    activitiesLoaded: Int,
    eventReceiver: EventReceiver<LoadActivitiesViewEvent>,
) {

    HighEmphasisButton(
        text = stringResource(R.string.button_continue),
        modifier = Modifier.defaultMinSize(
            minWidth = dimensionResource(id = R.dimen.button_min_width)
        ),
        size = LARGE
    ) {
        eventReceiver.onEventDebounced(ContinueClicked)
    }
}