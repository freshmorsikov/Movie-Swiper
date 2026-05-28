package com.github.freshmorsikov.moviematcher.core.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.freshmorsikov.moviematcher.core.ui.theme.MovieTheme

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
        onClick = onClick
    ) {
        Box(
            modifier = Modifier.padding(8.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = text,
                style = MovieTheme.typography.label16,
                color = color,
            )
        }
    }
}

@Preview
@Composable
private fun OutlinedMovieButtonPreview() {
    MovieTheme {
        OutlinedMovieButton(
            text = "This is button",
            onClick = {},
            color = MovieTheme.colors.stroke,
        )
    }
}