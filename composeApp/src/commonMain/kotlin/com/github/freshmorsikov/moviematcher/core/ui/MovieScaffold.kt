package com.github.freshmorsikov.moviematcher.core.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
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
        val layoutDirection = LocalLayoutDirection.current
        Box(
            modifier = Modifier.fillMaxSize()
                .padding(
                    start = padding.calculateStartPadding(layoutDirection = layoutDirection),
                    top = padding.calculateTopPadding(),
                    end = padding.calculateEndPadding(layoutDirection = layoutDirection),
                )
        ) {
            content()
        }
    }
}