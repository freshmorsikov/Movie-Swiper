package com.github.freshmorsikov.moviematcher.core.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.github.freshmorsikov.moviematcher.core.ui.theme.MovieTheme
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun OutlinedMovieButton(
    text: String,
    onClick: () -> Unit,
    color: Color,
    enabled: Boolean = true,
    modifier: Modifier = Modifier,
) {
    OutlinedButton(
        modifier = modifier,
        border = BorderStroke(
            width = 2.dp,
            color = color
        ),
        enabled = enabled,
        onClick = onClick,
    ) {
        Text(
            text = text,
            color = color,
            style = MovieTheme.typography.body14,
        )
    }
}

@Preview
@Composable
private fun OutlinedMovieButtonPreview() {
    MovieTheme {
        OutlinedMovieButton(
            text = "This is button",
            onClick = {},
            color = MovieTheme.colors.primary,
        )
    }
}