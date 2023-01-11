package com.activityartapp.presentation.welcomeScreen.composables

import androidx.annotation.Dimension
import androidx.annotation.Px
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.activityartapp.R
import com.activityartapp.architecture.EventReceiver
import com.activityartapp.presentation.common.button.ButtonSize
import com.activityartapp.presentation.common.button.HighEmphasisButton
import com.activityartapp.presentation.common.button.MediumEmphasisButton
import com.activityartapp.presentation.common.type.Subhead
import com.activityartapp.presentation.ui.theme.Silver
import com.activityartapp.presentation.welcomeScreen.WelcomeViewEvent

@Composable
fun WelcomeStandby(
    athleteName: String,
    athleteImageUrl: String,
    eventReceiver: EventReceiver<WelcomeViewEvent>
) {
    @Dimension val profilePictureSize = dimensionResource(id = R.dimen.profile_picture_size)
    @Px val profilePictureSizePx = LocalDensity.current.run {
        profilePictureSize.toPx().toInt()
    }

    Card(backgroundColor = colorResource(R.color.n20_icicle)) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {

            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(athleteImageUrl)
                    .size(size = profilePictureSizePx)
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(id = R.drawable.ic_avatar_profile),
                error = painterResource(id = R.drawable.ic_avatar_profile),
                fallback = painterResource(id = R.drawable.ic_avatar_profile),
                contentScale = ContentScale.FillBounds,
                contentDescription = stringResource(id = R.string.profile_picture_cd),
                modifier = Modifier
                    .size(profilePictureSize)
                    .clip(CircleShape)
                    .border(
                        width = dimensionResource(id = R.dimen.rounded_picture_stroke_width),
                        color = Silver,
                        shape = CircleShape
                    )
            )
            Subhead(text = athleteName)
        }
    }
    HighEmphasisButton(
        size = ButtonSize.LARGE,
        text = stringResource(id = R.string.welcome_button_make_art),
    ) { eventReceiver.onEventDebounced(WelcomeViewEvent.ClickedMakeArt) }
    MediumEmphasisButton(
        size = ButtonSize.LARGE,
        text = stringResource(id = R.string.welcome_button_about),
    ) { eventReceiver.onEventDebounced(WelcomeViewEvent.ClickedAbout) }
    MediumEmphasisButton(
        size = ButtonSize.LARGE,
        text = stringResource(id = R.string.welcome_button_logout),
    ) { eventReceiver.onEventDebounced(WelcomeViewEvent.ClickedLogout) }
}