package com.github.freshmorsikov.moviematcher.core.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.github.freshmorsikov.moviematcher.core.ui.theme.MovieTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun MovieButton(
    text: String,
    onClick: () -> Unit,
    containerColor: Color,
    contentColor: Color,
    enabled: Boolean = true,
    modifier: Modifier = Modifier,
) {
    Button(
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor,
        ),
        enabled = enabled,
        onClick = onClick
    ) {
        Text(
            modifier = Modifier.padding(8.dp),
            text = text,
            style = MovieTheme.typography.label16,
        )
    }
}

@Preview
@Composable
private fun MovieButtonPreview() {
    MovieTheme {
        MovieButton(
            text = "This is button",
            onClick = {},
            containerColor = MovieTheme.colors.primary,
            contentColor = MovieTheme.colors.text.onAccent,
        )
    }
}