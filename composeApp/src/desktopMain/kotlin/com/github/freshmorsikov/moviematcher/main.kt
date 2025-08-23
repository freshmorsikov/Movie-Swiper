package com.github.freshmorsikov.moviematcher

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.github.freshmorsikov.moviematcher.app.App

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "moviematcher",
    ) {
        App()
    }
}