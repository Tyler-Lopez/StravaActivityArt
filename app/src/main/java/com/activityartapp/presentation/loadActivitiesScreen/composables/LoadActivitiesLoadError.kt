package com.activityartapp.presentation.loadActivitiesScreen.composables

import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.activityartapp.R
import com.activityartapp.architecture.EventReceiver
import com.activityartapp.presentation.common.button.ButtonSize
import com.activityartapp.presentation.common.button.ButtonSize.*
import com.activityartapp.presentation.common.button.HighEmphasisButton
import com.activityartapp.presentation.common.button.MediumEmphasisButton
import com.activityartapp.presentation.common.type.Subhead
import com.activityartapp.presentation.common.type.SubheadHeavy
import com.activityartapp.presentation.common.type.TitleFour
import com.activityartapp.presentation.common.type.TitleTwo
import com.activityartapp.presentation.loadActivitiesScreen.LoadActivitiesViewEvent
import com.activityartapp.presentation.loadActivitiesScreen.LoadActivitiesViewEvent.*
import com.activityartapp.presentation.welcomeScreen.WelcomeViewEvent

/**
 * Shown when some activities or no activities have loaded.
 * Prompts athlete to either continue (if [activitiesLoaded] > 0)
 * or to try again to load.
 */
@Composable
fun LoadActivitiesLoadError(
    retrying: Boolean,
    eventReceiver: EventReceiver<LoadActivitiesViewEvent>,
) {

    Card(
        backgroundColor = colorResource(R.color.n20_icicle),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    tint = colorResource(R.color.n90_coal),
                    contentDescription = null
                )
                SubheadHeavy(text = stringResource(R.string.welcome_no_internet_header))
            }
            Subhead(text = stringResource(R.string.welcome_no_internet_description))
        }
    }
    HighEmphasisButton(
        size = LARGE,
        enabled = !retrying,
        isLoading = retrying,
        text = stringResource(R.string.welcome_no_internet_retry_button)
    ) {
        //  eventReceiver.onEventDebounced(WelcomeViewEvent.ClickedRetryConnection)
    }
    MediumEmphasisButton(
        size = LARGE,
        enabled = !retrying,
        text = stringResource(R.string.loading_activities_no_internet_return_button)
    ) {
    }
}