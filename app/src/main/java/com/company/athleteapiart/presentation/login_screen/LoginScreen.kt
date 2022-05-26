package com.company.athleteapiart.presentation.login_screen

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.company.athleteapiart.R
import com.company.athleteapiart.Screen
import com.company.athleteapiart.presentation.common.LoadingComposable
import com.company.athleteapiart.presentation.composable.ComposableHeader
import com.company.athleteapiart.presentation.ui.spacing
import com.company.athleteapiart.presentation.login_screen.LoginScreenState.*
import com.company.athleteapiart.presentation.ui.theme.Icicle


/*

 LoginScreen

 This is the start of navigation, this screen handles authenticating the user with Strava.
 One authenticated, they are redirected to the Welcome Screen.

 https://developers.strava.com/guidelines/

 */

object HalfSizeShape : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline = Outline.Rectangle(
        Rect(
            left = size.width * .045f,
            right = size.width - (size.width * .040f),
            top = size.height * .070f,
            bottom = size.height - (size.height * .075f)
        )
    )
}

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
            verticalArrangement = Arrangement.Center,
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
                            .size(300.dp)
                            .padding(MaterialTheme.spacing.md)
                    )
                    ComposableHeader(
                        text = "ACTIVITY ART",
                        isBold = true,
                        center = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Image(
                        painter = painterResource(id = R.drawable.btn_strava_connectwith_orange),
                        contentDescription = "Connect with Strava",
                        modifier = Modifier
                            .width(300.dp)
                            .clip(HalfSizeShape)
                            .clickable {
                                println("Here intent is ${viewModel.loginIntent}")
                                onLoginIntent(viewModel.loginIntent)
                            }
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