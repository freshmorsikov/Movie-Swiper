package com.github.freshmorsikov.moviematcher.shared.ui.movie

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

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
                    .background(Color.LightGray, RoundedCornerShape(4.dp))
                    .padding(horizontal = 6.dp, vertical = 2.dp),
                text = genre,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.DarkGray,
            )
        }
    }
}