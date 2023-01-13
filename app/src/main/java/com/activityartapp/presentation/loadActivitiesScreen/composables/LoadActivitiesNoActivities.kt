package com.activityartapp.presentation.loadActivitiesScreen.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
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
import com.activityartapp.presentation.common.button.ButtonSize
import com.activityartapp.presentation.common.button.HighEmphasisButton
import com.activityartapp.presentation.common.button.MediumEmphasisButton
import com.activityartapp.presentation.common.type.Subhead
import com.activityartapp.presentation.common.type.SubheadHeavy
import com.activityartapp.presentation.common.type.TitleTwo
import com.activityartapp.presentation.loadActivitiesScreen.LoadActivitiesViewEvent

@Composable
fun LoadActivitiesNoActivities(eventReceiver: EventReceiver<LoadActivitiesViewEvent>) {
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
                SubheadHeavy(text = stringResource(R.string.loading_activities_no_activities_header))
            }
            Subhead(
                text = stringResource(id = R.string.loading_activities_no_activities_description)
            )
        }
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically)
    ) {
        HighEmphasisButton(
            size = ButtonSize.LARGE,
            text = stringResource(R.string.loading_activities_no_internet_return_button)
        ) {
            eventReceiver.onEventDebounced(LoadActivitiesViewEvent.ClickedReturn)
        }
    }
}