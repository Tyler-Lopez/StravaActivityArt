package com.company.athleteapiart.presentation.login_screen

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.company.athleteapiart.R
import com.company.athleteapiart.Screen
import com.company.athleteapiart.presentation.common.LoadingComposable
import com.company.athleteapiart.presentation.login_screen.LoginScreenState.*
import com.company.athleteapiart.presentation.ui.shapes.ClippedImageShape
import com.company.athleteapiart.presentation.ui.theme.*
import com.company.athleteapiart.util.Constants


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

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(Icicle),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .widthIn(360.dp, maxWidth * 0.8f),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
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
                        painterResource(id = R.drawable.ic_paint_brush_svgrepo_com),
                        "",
                        modifier = Modifier
                            .size(256.dp)
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
                            .width(300.dp)
                            .clip(ClippedImageShape)
                            .clickable { onLoginIntent(viewModel.loginIntent) }
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