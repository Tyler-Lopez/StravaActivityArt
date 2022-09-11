package com.company.activityart.presentation.welcome_screen.composables

import androidx.annotation.Dimension
import androidx.annotation.Px
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
import androidx.compose.ui.layout.ContentScale.Companion.FillBounds
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.company.activityart.R
import com.company.activityart.architecture.ViewEventListener
import com.company.activityart.presentation.common.button.ButtonSize.LARGE
import com.company.activityart.presentation.common.button.HighEmphasisButton
import com.company.activityart.presentation.common.button.MediumEmphasisButton
import com.company.activityart.presentation.common.type.Subhead
import com.company.activityart.presentation.common.type.TitleTwo
import com.company.activityart.presentation.ui.theme.Silver
import com.company.activityart.presentation.ui.theme.spacing
import com.company.activityart.presentation.welcome_screen.WelcomeViewEvent
import com.company.activityart.presentation.welcome_screen.WelcomeViewEvent.*

@Composable
fun WelcomeScreenStandby(
    athleteImageUrl: String,
    athleteName: String,
    eventReceiver: ViewEventListener<WelcomeViewEvent>,
) {
    @Dimension val strokeWidth = dimensionResource(id = R.dimen.rounded_picture_stroke_width)
    @Dimension val buttonMinWidth = dimensionResource(id = R.dimen.button_min_width)
    @Dimension val profilePictureSize = dimensionResource(id = R.dimen.profile_picture_size)
    @Px val profilePictureSizePx = LocalDensity.current.run {
        profilePictureSize.toPx().toInt()
    }

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
                width = strokeWidth,
                color = Silver,
                shape = CircleShape
            )
    )
    Column(
        horizontalAlignment = CenterHorizontally,
        verticalArrangement = spacedBy(spacing.small)
    ) {
        Subhead(text = stringResource(id = R.string.app_name))
        TitleTwo(text = athleteName)
    }
    eventReceiver.apply {
        HighEmphasisButton(
            enabled = true,
            size = LARGE,
            text = stringResource(id = R.string.welcome_button_make_art),
            modifier = Modifier.defaultMinSize(minWidth = buttonMinWidth)
        ) { onEventDebounced(ClickedMakeArt) }
        MediumEmphasisButton(
            enabled = true,
            size = LARGE,
            text = stringResource(id = R.string.welcome_button_about),
            modifier = Modifier.defaultMinSize(minWidth = buttonMinWidth)
        ) { onEventDebounced(ClickedAbout) }
        MediumEmphasisButton(
            enabled = true,
            size = LARGE,
            text = stringResource(id = R.string.welcome_button_logout),
            modifier = Modifier.defaultMinSize(minWidth = buttonMinWidth)
        ) { onEventDebounced(ClickedLogout) }
    }
}