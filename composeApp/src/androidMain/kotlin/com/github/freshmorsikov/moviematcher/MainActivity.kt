package com.github.freshmorsikov.moviematcher

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.github.freshmorsikov.moviematcher.app.App
import com.google.firebase.FirebaseApp
import org.koin.dsl.module

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        FirebaseApp.initializeApp(this)
        setContent {
            App(contextModule = contextModule())
        }
    }

    private fun contextModule() = module {
        single<Context> {
            application.applicationContext
        }
    }

}

@Preview
@Composable
private fun AppAndroidPreview() {
    App()
}