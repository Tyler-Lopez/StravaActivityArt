package com.activityartapp.presentation.loginScreen

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.activityartapp.R
import com.activityartapp.architecture.EventReceiver
import com.activityartapp.presentation.common.ConnectWithStravaButton
import com.activityartapp.presentation.ui.theme.spacing
import com.google.maps.android.data.Feature

/**
 * When the athlete is determined as unauthenticated,
 * this screen is the start destination.
 *
 * Presents a button which will prompt the athlete to
 * "Connect with Strava".
 */
@Composable
fun LoginScreen(viewModel: LoginViewModel) {
    var orientation by remember { mutableStateOf(Configuration.ORIENTATION_PORTRAIT) }

    val configuration = LocalConfiguration.current

    // If our configuration changes then this will launch a new coroutine scope for it
    LaunchedEffect(configuration) {
        // Save any changes to the orientation value on the configuration object
        snapshotFlow { configuration.orientation }
            .collect { orientation = it }
    }

    Surface {
        when (orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> {
                Row(modifier = Modifier.fillMaxSize()) {
                    Content(
                        imageBoxModifier = Modifier.weight(1f, false),
                        isLandscape = true,
                        viewModel = viewModel
                    )
                }
            }
            else -> {
                Column(modifier = Modifier.fillMaxSize()) {
                    Content(
                        imageBoxModifier = Modifier.weight(1f, false),
                        isLandscape = false,
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}

@Composable
private fun FeatureGraphic() {
    Image(
        painterResource(R.drawable.display_image),
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxSize(),
        alignment = Alignment.Center,
        contentDescription = stringResource(R.string.content_description_feature_graphic)
    )
}

@Composable
private fun ConnectWithStravaSection(
    viewModel: EventReceiver<LoginViewEvent>
) {
    Image(
        painter = painterResource(id = R.drawable.ic_activity_art_logo),
        contentDescription = stringResource(R.string.app_logo_content_description)
    )
    Text(
        text = stringResource(id = R.string.app_description),
        modifier = Modifier.sizeIn(maxWidth = 254.dp),
        style = MaterialTheme.typography.body1,
        textAlign = TextAlign.Center
    )
    ConnectWithStravaButton {
        viewModel.onEvent(LoginViewEvent.ConnectWithStravaClicked)
    }
}


@Composable
private fun Content(
    imageBoxModifier: Modifier,
    isLandscape: Boolean,
    viewModel: LoginViewModel
) {
    Box(modifier = imageBoxModifier) {
        FeatureGraphic()
    }
    Spacer(modifier = Modifier
        .run {
            if (isLandscape) {
                width(1.dp).fillMaxHeight()
            } else {
                height(1.dp).fillMaxWidth()
            }
        }
        .background(MaterialTheme.colors.primary))
    Column(
        modifier = Modifier
            .padding(spacing.medium)
            .run {
                if (isLandscape) {
                    fillMaxHeight()
                } else {
                    fillMaxWidth()
                }
            },
        verticalArrangement = Arrangement.spacedBy(spacing.medium, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ConnectWithStravaSection(viewModel = viewModel)
    }
}