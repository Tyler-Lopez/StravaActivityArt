package com.company.activityart.presentation.welcomeScreen

import androidx.annotation.Dimension
import androidx.annotation.Px
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale.Companion.FillBounds
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.company.activityart.R
import com.company.activityart.presentation.common.button.ButtonSize
import com.company.activityart.presentation.common.button.HighEmphasisButton
import com.company.activityart.presentation.common.button.MediumEmphasisButton
import com.company.activityart.presentation.common.type.*
import com.company.activityart.presentation.editArtScreen.subscreens.filters.composables.ScreenBackground
import com.company.activityart.presentation.ui.theme.Silver


/*

Welcome Screen
This is the screen users who are authenticated see first on opening the app.

https://developers.strava.com/guidelines/

 */

@Composable
fun WelcomeScreen(viewModel: WelcomeViewModel) {
    ScreenBackground {
        viewModel.viewState.collectAsState().value?.apply {
            @Dimension val profilePictureSize = dimensionResource(id = R.dimen.profile_picture_size)
            @Px val profilePictureSizePx = LocalDensity.current.run {
                profilePictureSize.toPx().toInt()
            }

            Image(painterResource(id = R.drawable.ic_activity_art_logo), contentDescription = null)
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
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
                    contentScale = FillBounds,
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
            HighEmphasisButton(
                size = ButtonSize.LARGE,
                text = stringResource(id = R.string.welcome_button_make_art),
            ) { viewModel.onEventDebounced(WelcomeViewEvent.ClickedMakeArt) }
            MediumEmphasisButton(
                size = ButtonSize.LARGE,
                text = stringResource(id = R.string.welcome_button_about),
            ) { viewModel.onEventDebounced(WelcomeViewEvent.ClickedAbout) }
            MediumEmphasisButton(
                size = ButtonSize.LARGE,
                text = stringResource(id = R.string.welcome_button_logout),
            ) { viewModel.onEventDebounced(WelcomeViewEvent.ClickedLogout) }
        }
    }
}
