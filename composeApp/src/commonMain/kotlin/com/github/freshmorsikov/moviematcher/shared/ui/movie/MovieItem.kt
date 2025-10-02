package com.github.freshmorsikov.moviematcher.shared.ui.movie

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.github.freshmorsikov.moviematcher.core.data.api.IMAGE_BASE_URL
import com.github.freshmorsikov.moviematcher.shared.domain.model.Movie
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun MovieItem(
    movie: Movie,
    onClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.clickable(
            onClick = {
                onClick(movie.id)
            }
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, Color.Black.copy(alpha = 0.1f))
    ) {
        Row(modifier = Modifier.height(IntrinsicSize.Min)) {
            AsyncImage(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(120.dp),
                model = "$IMAGE_BASE_URL${movie.posterPath}",
                contentScale = ContentScale.Fit,
                contentDescription = null,
            )
            Column(
                modifier = Modifier.padding(
                    start = 8.dp,
                    top = 8.dp,
                    end = 12.dp,
                    bottom = 8.dp,
                ),
                verticalArrangement = spacedBy(4.dp)
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = movie.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                MovieInfo(
                    releaseDate = movie.releaseDate,
                    voteAverage = movie.voteAverage,
                    voteCount = movie.voteCount,
                )
                MovieGenres(genres = movie.genres)
            }
        }
    }
}

@Preview
@Composable
private fun MovieItemPreview() {
    MaterialTheme {
        MovieItem(
            movie = Movie.mock,
            onClick = {},
        )
    }
}