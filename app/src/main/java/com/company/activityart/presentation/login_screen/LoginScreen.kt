package com.company.activityart.presentation.login_screen

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.company.activityart.R
import com.company.activityart.Screen
import com.company.activityart.presentation.common.ContainerColumn
import com.company.activityart.presentation.common.LoadingComposable
import com.company.activityart.presentation.login_screen.LoginScreenState.*
import com.company.activityart.presentation.ui.shapes.ClippedImageShape
import com.company.activityart.presentation.ui.theme.*
import com.company.activityart.util.Constants


/*

 LoginScreen

 This is the start of navigation, this screen handles authenticating the user with Strava.
 One authenticated, they are redirected to the Welcome Screen.

 https://developers.strava.com/guidelines/

 */

@Composable
fun LoginScreen(
    uri: Uri?,
    navController: NavHostController,
    onLoginIntent: (Intent) -> Unit,
    viewModel: LoginScreenViewModel = hiltViewModel()
) {
    val screenState by remember { viewModel.loginScreenState }
    val context = LocalContext.current

    val stravaButtonWidth = with(LocalDensity.current) { 386f.toDp() }
    val stravaButtonHeight = with(LocalDensity.current) { 96f.toDp() }
    val stravaButtonMargin = with(LocalDensity.current) { 16f.toDp() }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(Icicle),
        contentAlignment = Alignment.Center
    ) {
        ContainerColumn(maxWidth) {
            when (screenState) {
                // Just launched Login screen, check URI for access code
                // In future, check ROOM database for previous code
                // SideEffect composable invoked on every recomposition
                // Necessary to ensure we recompose screenState
                LAUNCH -> SideEffect {
                    viewModel.attemptGetAccessToken(
                        uri = uri,
                        context = context
                    )
                }

                // In process of trying to get response from Strava where we input in URI
                LOADING -> {
                    // Loading Screen Composable goes here later
                    LoadingComposable()
                }
                // Wait for user to press Login
                STANDBY -> {
                    Image(
                        painterResource(id = R.drawable.ic_frameactivityart),
                        "",
                        modifier = Modifier
                            .width(stravaButtonWidth - stravaButtonMargin),
                        contentScale = ContentScale.FillWidth
                    )
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "${Constants.STAGE.uppercase()} " + Constants.VERSION,
                            fontSize = 18.sp,
                            fontFamily = Lato,
                            color = Gravel,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = Constants.APP_NAME,
                            fontSize = 32.sp,
                            fontFamily = MaisonNeue,
                            color = Asphalt,
                            fontWeight = FontWeight.Black,
                        )
                    }
                    Image(
                        painter = painterResource(id = R.drawable.btn_strava_connectwith_orange),
                        contentDescription = "Connect with Strava",
                        modifier = Modifier
                            .width(stravaButtonWidth)
                            .height(stravaButtonHeight)
                            .clip(ClippedImageShape)
                            .clickable { onLoginIntent(viewModel.loginIntent) },
                        contentScale = ContentScale.FillBounds
                    )
                }
                // User has been successfully authorized
                // Redirect them to the welcome screen
                AUTHORIZED ->
                    LaunchedEffect(Unit) {
                        (navController.currentBackStackEntry?.destination?.route == Screen.Login.route)
                        navController.navigate(
                            route = Screen.Welcome.withArgs(
                                // * Is the spread operator to convert an Array<String> into vararg String
                                *viewModel.getNavArgs()
                            )
                        ) {
                            popUpTo(Screen.Login.route) {
                                inclusive = true
                            }
                        }
                    }
            }
        }
    }
}