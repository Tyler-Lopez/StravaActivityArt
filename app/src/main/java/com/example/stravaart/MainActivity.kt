package com.example.stravaart

import android.content.Intent
import android.os.Bundle
import android.util.AttributeSet
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.stravaart.ui.theme.StravaArtTheme
import com.sweetzpot.stravazpot.authenticaton.api.AccessScope
import com.sweetzpot.stravazpot.authenticaton.api.ApprovalPrompt
import com.sweetzpot.stravazpot.authenticaton.api.StravaLogin
import com.sweetzpot.stravazpot.authenticaton.ui.StravaLoginButton

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StravaArtTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    Row(modifier = Modifier.fillMaxSize()) {
                        val intent: Intent = StravaLogin
                            .withContext(this@MainActivity)
                            .withClientID(5)
                            .withRedirectURI("test")
                            .withApprovalPrompt(ApprovalPrompt.AUTO)
                            .withAccessScope(AccessScope.VIEW_PRIVATE_WRITE)
                            .makeIntent()
                        startActivity(intent)
                        Text("hello")
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
    StravaArtTheme {
        Greeting("Android")
    }
}