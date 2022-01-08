package com.example.stravaart

import android.accounts.Account
import android.accounts.AccountManager
import android.accounts.AccountManagerCallback
import android.accounts.AccountManagerFuture
import android.content.Intent
import android.media.session.MediaSessionManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.stravaart.ui.theme.StravaArtTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
    // https://developers.strava.com/docs/authentication/
        val intentUri = Uri.parse("https://www.strava.com/oauth/mobile/authorize")
            .buildUpon()
            .appendQueryParameter("client_id", "75992")
       //     .appendQueryParameter("redirect_uri", "https://www.yourapp.com")
       //     .appendQueryParameter("response_type", "code")
            .appendQueryParameter("approval_prompt", "auto")
            .appendQueryParameter("scope", "activity:read_all")
            .build()
        val intent = Intent(Intent.ACTION_VIEW, intentUri)


        super.onCreate(savedInstanceState)
        setContent {
            StravaArtTheme {
                startActivity(intent)

            }
        }
    }
}


@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    StravaArtTheme {
        Greeting("Android")
    }
}