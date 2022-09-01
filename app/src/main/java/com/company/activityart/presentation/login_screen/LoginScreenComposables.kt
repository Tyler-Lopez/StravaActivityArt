package com.company.activityart.presentation.login_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.company.activityart.R
import com.company.activityart.architecture.ViewEventListener
import com.company.activityart.presentation.common.type.Subhead
import com.company.activityart.presentation.common.type.TitleOne
import com.company.activityart.presentation.login_screen.LoginScreenViewEvent.ConnectWithStravaClicked
import com.company.activityart.presentation.ui.theme.*
import com.company.activityart.util.Constants
import com.company.activityart.util.Constants.APP_NAME
import com.company.activityart.util.Constants.STAGE
import com.company.activityart.util.Constants.VERSION

@Composable
fun LoginScreenStandbyState(
    eventReceiver: ViewEventListener<LoginScreenViewEvent>
) {
    Column(
        modifier = Modifier.padding(spacing.medium),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Subhead(text = "$STAGE $VERSION")
        TitleOne(text = APP_NAME)
    }
    Image(
        painter = painterResource(id = R.drawable.ic_btn_strava_connectwith_orange_clipped),
        contentDescription = stringResource(id = R.string.connect_with_strava_button_cd),
        modifier = Modifier
            .clickable { eventReceiver.onEvent(ConnectWithStravaClicked) },
    )
}
