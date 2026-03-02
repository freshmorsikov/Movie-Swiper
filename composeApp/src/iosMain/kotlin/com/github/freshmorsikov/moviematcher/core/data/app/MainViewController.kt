package com.github.freshmorsikov.moviematcher.core.data.app

import androidx.compose.ui.window.ComposeUIViewController
import com.github.freshmorsikov.moviematcher.app.App
import com.github.freshmorsikov.moviematcher.core.ui.theme.MovieTheme

fun MainViewController() = ComposeUIViewController {
    MovieTheme {
        App()
    }
}