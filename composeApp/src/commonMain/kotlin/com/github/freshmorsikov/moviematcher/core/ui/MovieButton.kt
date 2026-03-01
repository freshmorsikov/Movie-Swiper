package com.github.freshmorsikov.moviematcher.core.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import com.github.freshmorsikov.moviematcher.core.ui.theme.MovieTheme
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun PrimaryMovieButton(
    text: String,
    onClick: () -> Unit,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    modifier: Modifier = Modifier,
) = MovieButton(
    text = text,
    onClick = onClick,
    colors = ButtonDefaults.buttonColors(
        containerColor = MovieTheme.colors.primary,
        contentColor = MovieTheme.colors.text.onAccent,
        disabledContainerColor = MovieTheme.colors.primaryDisabled,
        disabledContentColor = MovieTheme.colors.text.onAccent,
    ),
    enabled = enabled,
    isLoading = isLoading,
    modifier = modifier,
)

@Composable
fun MovieButton(
    text: String,
    onClick: () -> Unit,
    colors: ButtonColors,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    modifier: Modifier = Modifier,
) {
    Button(
        modifier = modifier,
        colors = colors,
        enabled = enabled && !isLoading,
        onClick = onClick
    ) {
        Box(
            modifier = Modifier.padding(8.dp),
            contentAlignment = Alignment.Center,
        ) {
            val alpha = if (isLoading) 0f else 1f
            Text(
                modifier = Modifier.alpha(alpha = alpha),
                text = text,
                style = MovieTheme.typography.label16,
            )
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(16.dp),
                    color = LocalContentColor.current,
                    strokeWidth = 2.dp,
                )
            }
        }
    }
}

@Preview
@Composable
private fun PrimaryMovieButtonPreview() {
    MovieTheme {
        PrimaryMovieButton(
            text = "This is button",
            onClick = {},
        )
    }
}

@Preview
@Composable
private fun PrimaryMovieButtonLoadingPreview() {
    MovieTheme {
        PrimaryMovieButton(
            text = "This is button",
            onClick = {},
            isLoading = true,
        )
    }
}
