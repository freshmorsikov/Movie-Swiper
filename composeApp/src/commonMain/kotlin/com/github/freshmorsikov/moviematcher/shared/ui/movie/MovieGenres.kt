package com.github.freshmorsikov.moviematcher.shared.ui.movie

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.freshmorsikov.moviematcher.core.ui.theme.MovieTheme

@Composable
fun MovieGenres(
    genres: List<String>,
    modifier: Modifier = Modifier,
) {
    FlowRow(
        modifier = modifier,
        horizontalArrangement = spacedBy(8.dp),
        verticalArrangement = spacedBy(8.dp),
    ) {
        genres.forEach { genre ->
            Text(
                modifier = Modifier
                    .background(
                        color = MovieTheme.colors.background,
                        shape = RoundedCornerShape(4.dp),
                    )
                    .padding(
                        horizontal = 6.dp,
                        vertical = 2.dp
                    ),
                text = genre,
                style = MovieTheme.typography.body14,
                color = MovieTheme.colors.text.variant,
            )
        }
    }
}