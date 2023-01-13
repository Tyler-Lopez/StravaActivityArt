package com.activityartapp.presentation.loadActivitiesScreen.composables

import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.activityartapp.R
import com.activityartapp.architecture.EventReceiver
import com.activityartapp.presentation.common.button.ButtonSize.*
import com.activityartapp.presentation.common.button.HighEmphasisButton
import com.activityartapp.presentation.common.button.LowEmphasisButton
import com.activityartapp.presentation.common.button.MediumEmphasisButton
import com.activityartapp.presentation.common.type.Subhead
import com.activityartapp.presentation.common.type.SubheadHeavy
import com.activityartapp.presentation.common.type.TitleTwo
import com.activityartapp.presentation.loadActivitiesScreen.LoadActivitiesViewEvent

/**
 * Shown when some activities or no activities have loaded.
 * Prompts athlete to either continue (if [activitiesLoaded] > 0)
 * or to try again to load.
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LoadActivitiesLoadError(
    activitiesLoaded: Int,
    retrying: Boolean,
    eventReceiver: EventReceiver<LoadActivitiesViewEvent>,
) {

    TitleTwo(text = stringResource(R.string.loading_activities_error_header))
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
            Subhead(
                text = activitiesLoaded.takeIf { it > 0 }?.let {
                    pluralStringResource(
                        id = R.plurals.loading_activities_no_internet_prompt,
                        count = activitiesLoaded,
                        activitiesLoaded
                    )
                } ?: stringResource(id = R.string.loading_activities_no_internet_prompt_zero_count)
            )
        }
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically)
    ) {
        activitiesLoaded.takeIf { it > 0 }?.let {
            HighEmphasisButton(
                size = LARGE,
                enabled = !retrying,
                text = stringResource(R.string.loading_activities_no_internet_continue_button)
            ) {
                eventReceiver.onEventDebounced(LoadActivitiesViewEvent.ClickedContinue)
            }
        }
        MediumEmphasisButton(
            size = LARGE,
            enabled = !retrying,
            isLoading = retrying,
            text = stringResource(R.string.loading_activities_no_internet_retry_button)
        ) {
            eventReceiver.onEventDebounced(LoadActivitiesViewEvent.ClickedRetry)
        }
       MediumEmphasisButton(
            size = LARGE,
            enabled = !retrying,
            text = stringResource(R.string.loading_activities_no_internet_return_button)
        ) {
            eventReceiver.onEventDebounced(LoadActivitiesViewEvent.ClickedReturn)
        }
    }
}