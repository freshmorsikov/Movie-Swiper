package com.github.freshmorsikov.moviematcher.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.freshmorsikov.moviematcher.core.ui.theme.MovieTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun LoadingContent(modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .background(
                    color = MovieTheme.colors.surface.main,
                    shape = RoundedCornerShape(8.dp)
                )
        ) {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(16.dp)
                    .size(24.dp),
                color = MovieTheme.colors.primary,
                strokeWidth = 4.dp,
            )
        }
    }
}

@Preview
@Composable
private fun LoadingContentPreview() {
    MovieTheme {
        LoadingContent(modifier = Modifier.fillMaxSize())
    }
}