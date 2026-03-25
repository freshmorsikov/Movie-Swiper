package com.github.freshmorsikov.moviematcher.core.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.freshmorsikov.moviematcher.core.ui.theme.MovieTheme
import moviematcher.composeapp.generated.resources.Res
import moviematcher.composeapp.generated.resources.ic_back
import org.jetbrains.compose.resources.painterResource

@Composable
fun MovieBackButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    FilledIconButton(
        modifier = modifier
            .size(48.dp)
            .border(
                width = 1.dp,
                color = MovieTheme.colors.surface.main.copy(alpha = 0.8f),
                shape = CircleShape,
            ),
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = MovieTheme.colors.surface.main.copy(alpha = 0.6f),
        ),
        onClick = onClick,
    ) {
        Icon(
            modifier = Modifier.size(24.dp),
            painter = painterResource(Res.drawable.ic_back),
            tint = MovieTheme.colors.icon.main,
            contentDescription = null,
        )
    }
}
