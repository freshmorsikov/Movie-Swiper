package com.github.freshmorsikov.moviematcher.feature.swipe

import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.github.freshmorsikov.moviematcher.core.data.api.IMAGE_BASE_URL
import com.github.freshmorsikov.moviematcher.core.ui.MovieButton
import com.github.freshmorsikov.moviematcher.core.ui.MovieScaffold
import com.github.freshmorsikov.moviematcher.feature.swipe.domain.model.Movie
import com.github.freshmorsikov.moviematcher.feature.swipe.presentation.SwipeUdf
import com.github.freshmorsikov.moviematcher.feature.swipe.presentation.SwipeViewModel
import com.github.freshmorsikov.moviematcher.util.toRatingFormat
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SwipeScreen(
    viewModel: SwipeViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    SwipeScreenContent(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun SwipeScreenContent(
    state: SwipeUdf.State,
    onAction: (SwipeUdf.Action) -> Unit
) {
    MovieScaffold {
        state.movieList.forEach { movie ->
            MovieCard(
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.Center),
                movie = movie
            )
        }

        Row(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.BottomCenter),
            horizontalArrangement = spacedBy(16.dp)
        ) {
            MovieButton(
                modifier = Modifier.weight(1f),
                text = "Dislike",
                containerColor = Color(0xFFF95667),
                onClick = {
                    onAction(SwipeUdf.Action.Dislike)
                }
            )
            MovieButton(
                modifier = Modifier.weight(1f),
                text = "Like",
                containerColor = Color(0xFF00BE64),
                onClick = {
                    onAction(SwipeUdf.Action.Like)
                }
            )
        }
    }
}

@Composable
private fun MovieCard(
    movie: Movie,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = Color.LightGray
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 12.dp,
                    vertical = 8.dp
                )
        ) {
            AsyncImage(
                modifier = Modifier.height(500.dp),
                model = "$IMAGE_BASE_URL${movie.posterPath}",
                contentScale = ContentScale.FillHeight,
                contentDescription = null,
            )
            Text(text = movie.title)
            if (movie.originalTitle != movie.title) {
                Text(text = movie.originalTitle)
            }
            Text(text = movie.releaseDate)
            Text(text = "${movie.voteAverage.toRatingFormat()} / 10")
        }
    }
}

@Preview
@Composable
private fun SwipeScreenContentPreview() {
    SwipeScreenContent(
        state = SwipeUdf.State(
            movieList = emptyList()
        ),
        onAction = {}
    )
}