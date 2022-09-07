package com.company.activityart.presentation.login_screen.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.company.activityart.R
import com.company.activityart.architecture.ViewEventListener
import com.company.activityart.presentation.common.type.Subhead
import com.company.activityart.presentation.common.type.TitleOne
import com.company.activityart.presentation.login_screen.LoginScreenViewEvent
import com.company.activityart.presentation.login_screen.LoginScreenViewEvent.*
import com.company.activityart.presentation.ui.theme.spacing
import com.company.activityart.util.StringConstants

@Composable
fun LoginScreenStandby(
    eventReceiver: ViewEventListener<LoginScreenViewEvent>
) {
    Column(
        modifier = Modifier.padding(spacing.medium),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Subhead(text = "${StringConstants.STAGE} ${StringConstants.VERSION}")
        TitleOne(text = stringResource(id = R.string.app_name))
    }
    Image(
        painter = painterResource(id = R.drawable.ic_btn_strava_connectwith_orange_clipped),
        contentDescription = stringResource(id = R.string.connect_with_strava_button_cd),
        modifier = Modifier
            .clickable { eventReceiver.onEvent(ConnectWithStravaClicked) },
    )
}