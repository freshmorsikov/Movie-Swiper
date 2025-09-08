package com.github.freshmorsikov.moviematcher

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowInsetsControllerCompat
import com.github.freshmorsikov.moviematcher.app.App
import com.google.firebase.FirebaseApp
import org.koin.dsl.module

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setupEdgeToEdge()

        super.onCreate(savedInstanceState)

        FirebaseApp.initializeApp(this)
        setContent {
            MaterialTheme {
                App(contextModule = contextModule())
            }
        }
    }

    private fun contextModule() = module {
        single<Context> {
            application.applicationContext
        }
    }

    private fun setupEdgeToEdge() {
        enableEdgeToEdge(
            navigationBarStyle = SystemBarStyle.auto(
                lightScrim = Color.TRANSPARENT,
                darkScrim = Color.TRANSPARENT
            )
        )
        WindowInsetsControllerCompat(window, window.decorView).apply {
            isAppearanceLightStatusBars = true
            isAppearanceLightNavigationBars = true
        }
    }

}

@Preview
@Composable
private fun AppAndroidPreview() {
    App()
}