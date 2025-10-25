package com.github.freshmorsikov.moviematcher

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowInsetsControllerCompat
import com.github.freshmorsikov.moviematcher.app.App
import com.github.freshmorsikov.moviematcher.core.ui.theme.MovieTheme
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.analytics.analytics

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setupEdgeToEdge()

        super.onCreate(savedInstanceState)

        setContent {
            MovieTheme {
                App()
            }
        }

        FirebaseApp.initializeApp(this)
        Firebase.analytics.setAnalyticsCollectionEnabled(!BuildConfig.DEBUG)
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