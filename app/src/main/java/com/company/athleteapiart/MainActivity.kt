package com.company.athleteapiart

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.company.athleteapiart.ui.theme.AthleteApiArtTheme

class MainActivity : ComponentActivity() {
    val intentUri = Uri.parse("https://www.strava.com/oauth/mobile/authorize")
        .buildUpon()
        .appendQueryParameter("client_id", "75992")
        .appendQueryParameter("redirect_uri", "com.company.athleteapiart://myapp.com")
        .appendQueryParameter("response_type", "code")
        .appendQueryParameter("approval_prompt", "auto")
        .appendQueryParameter("scope", "activity:read_all")
        .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AthleteApiArtTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    Button(onClick = {
                        val intent = Intent(Intent.ACTION_VIEW, intentUri)
                        startActivity(intent)
                    }) {
                        Text("Login with Strava")
                    }
                }
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
    AthleteApiArtTheme {
        Greeting("Android")
    }
}