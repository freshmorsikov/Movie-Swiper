package com.github.freshmorsikov.moviematcher.shared.ui.player

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.ui.PlayerView

@Composable
actual fun MoviePlayerView(
    modifier: Modifier,
    controller: PlayerStateController
) {
    val mediaController = remember {
        mutableStateOf(controller.mediaController)
    }
    DisposableEffect(Unit) {
        onDispose {
            controller.release()
        }
    }
    AndroidView(
        modifier = modifier,
        factory = { ctx ->
            PlayerView(ctx).apply {
                player = mediaController.value
                useController = false
            }
        },
    )
}