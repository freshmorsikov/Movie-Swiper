package com.github.freshmorsikov.moviematcher.shared.ui.player

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun MoviePlayerView(
    modifier: Modifier,
    controller: PlayerStateController
)