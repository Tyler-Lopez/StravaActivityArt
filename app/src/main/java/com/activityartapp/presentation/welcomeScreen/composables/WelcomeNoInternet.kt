package com.activityartapp.presentation.welcomeScreen.composables

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.activityartapp.R
import com.activityartapp.architecture.EventReceiver
import com.activityartapp.presentation.common.button.ButtonSize
import com.activityartapp.presentation.common.button.HighEmphasisButton
import com.activityartapp.presentation.common.type.Subhead
import com.activityartapp.presentation.common.type.SubheadHeavy
import com.activityartapp.presentation.welcomeScreen.WelcomeViewEvent

// todo add retry failed toast...
@Composable
fun WelcomeNoInternet(
    retrying: Boolean,
    eventReceiver: EventReceiver<WelcomeViewEvent>
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
        size = ButtonSize.LARGE,
        enabled = !retrying,
        isLoading = retrying,
        text = stringResource(R.string.welcome_no_internet_retry_button)
    ) {
        eventReceiver.onEventDebounced(WelcomeViewEvent.ClickedRetryConnection)
    }
}