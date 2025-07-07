package com.github.freshmorsikov.moviematcher

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "moviematcher",
    ) {
        App()
    }
}