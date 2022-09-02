package com.company.activityart.presentation.welcome_screen.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import coil.compose.rememberAsyncImagePainter
import com.company.activityart.R
import com.company.activityart.architecture.ViewEventListener
import com.company.activityart.presentation.common.button.ButtonSize.*
import com.company.activityart.presentation.common.button.HighEmphasisButton
import com.company.activityart.presentation.common.type.Subhead
import com.company.activityart.presentation.common.type.TitleTwo
import com.company.activityart.presentation.ui.theme.StravaOrange
import com.company.activityart.presentation.ui.theme.spacing
import com.company.activityart.presentation.welcome_screen.WelcomeScreenViewEvent
import com.company.activityart.presentation.welcome_screen.WelcomeScreenViewEvent.*
import com.company.activityart.presentation.welcome_screen.WelcomeScreenViewState


@Composable
fun WelcomeScreenStandby(
    state: WelcomeScreenViewState.Standby,
    eventReceiver: ViewEventListener<WelcomeScreenViewEvent>,
) {
    val profilePictureSize = dimensionResource(id = R.dimen.profile_picture_size)
    val strokeWidth = dimensionResource(id = R.dimen.rounded_picture_stroke_width)
    val buttonMinWidth = dimensionResource(id = R.dimen.button_min_width)

    Image(
        painter = rememberAsyncImagePainter(state.athleteImageUrl),
        contentDescription = stringResource(id = R.string.profile_picture_cd),
        modifier = Modifier
            .size(profilePictureSize)
            .clip(CircleShape)
            .border(
                width = strokeWidth,
                color = StravaOrange,
                shape = CircleShape
            )
    )
    Column(
        horizontalAlignment = CenterHorizontally,
        verticalArrangement = spacedBy(spacing.small)
    ) {
        Subhead(text = stringResource(id = R.string.app_name))
        TitleTwo(text = state.athleteName)
    }
    eventReceiver.apply {
        HighEmphasisButton(
            enabled = true,
            size = LARGE,
            text = stringResource(id = R.string.welcome_button_make_art),
            modifier = Modifier.defaultMinSize(minWidth = buttonMinWidth)
        ) { onEvent(ClickedMakeArt) }
        HighEmphasisButton(
            enabled = true,
            size = LARGE,
            text = stringResource(id = R.string.welcome_button_about),
            modifier = Modifier.defaultMinSize(minWidth = buttonMinWidth)
        ) { onEvent(ClickedAbout) }
        HighEmphasisButton(
            enabled = true,
            size = LARGE,
            text = stringResource(id = R.string.welcome_button_logout),
            modifier = Modifier.defaultMinSize(minWidth = buttonMinWidth)
        ) { onEvent(ClickedLogout) }
    }
}