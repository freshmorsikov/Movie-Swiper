package com.github.freshmorsikov.moviematcher.core.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.github.freshmorsikov.moviematcher.core.ui.theme.MovieTheme

@Composable
fun MovieScaffold(
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit = {},
    background: Color = MovieTheme.colors.background,
    contentWindowInsets: WindowInsets = WindowInsets.none,
    content: @Composable BoxScope.() -> Unit,
) {
    Scaffold(
        modifier = modifier,
        topBar = topBar,
        contentWindowInsets = contentWindowInsets,
        containerColor = background,
    ) { padding ->
        Box(
            modifier = Modifier.fillMaxSize()) {
            content()
        }
    }
}
