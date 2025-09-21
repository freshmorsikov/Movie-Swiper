package com.github.freshmorsikov.moviematcher.feature.details

import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.github.freshmorsikov.moviematcher.core.data.api.IMAGE_BASE_URL
import com.github.freshmorsikov.moviematcher.core.ui.MovieScaffold
import com.github.freshmorsikov.moviematcher.core.ui.Shimmer
import com.github.freshmorsikov.moviematcher.core.ui.none
import com.github.freshmorsikov.moviematcher.feature.details.presentation.MovieDetailsUdf
import com.github.freshmorsikov.moviematcher.feature.details.presentation.MovieDetailsViewModel
import com.github.freshmorsikov.moviematcher.shared.domain.model.Movie
import com.github.freshmorsikov.moviematcher.shared.ui.movie.MovieGenres
import com.github.freshmorsikov.moviematcher.shared.ui.movie.MovieInfo
import com.github.freshmorsikov.moviematcher.util.toAmountFormat
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun MovieDetailsScreen(
    movieId: Long,
    viewModel: MovieDetailsViewModel = koinViewModel(
        parameters = { parametersOf(movieId) }
    )
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    MovieDetailsScreenContent(
        state = state,
        onAction = viewModel::onAction,
    )
}

@Composable
private fun MovieDetailsScreenContent(
    state: MovieDetailsUdf.State,
    onAction: (MovieDetailsUdf.Action) -> Unit
) {
    MovieScaffold(contentWindowInsets = WindowInsets.none) {
        when (state) {
            MovieDetailsUdf.State.Loading -> {
                LoadingMovieDetailsScreenContent()
            }

            is MovieDetailsUdf.State.Data -> {
                LoadedMovieDetailsScreenContent(
                    state = state,
                    onAction = onAction,
                )
            }
        }
    }
}

@Composable
private fun LoadingMovieDetailsScreenContent() {

}

@Composable
private fun LoadedMovieDetailsScreenContent(
    state: MovieDetailsUdf.State.Data,
    onAction: (MovieDetailsUdf.Action) -> Unit,
) {
    Column {
        AsyncImage(
            modifier = Modifier.fillMaxWidth(),
            model = "$IMAGE_BASE_URL${state.movie.posterPath}",
            contentScale = ContentScale.FillBounds,
            contentDescription = null,
        )
        Column(
            modifier = Modifier
                .width(380.dp)
                .padding(16.dp),
            verticalArrangement = spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = state.movie.title,
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
            )
            MovieInfo(
                releaseDate = state.movie.releaseDate,
                voteAverage = state.movie.voteAverage,
                voteCount = state.movie.voteCount,
                runtime = state.movie.runtime,
            )
            MovieGenres(state.movie.genres)

            if (state.movie.overview == null) {
                Shimmer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(20.dp)
                )
            } else {
                Column(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "Overview",
                        style = MaterialTheme.typography.titleMedium,
                    )
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = state.movie.overview,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
            if (state.movie.budget == null) {
                Shimmer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(20.dp)
                )
            } else if (state.movie.budget > 0) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Budget",
                        style = MaterialTheme.typography.titleMedium,
                    )
                    Text(
                        text = "$${state.movie.budget.toAmountFormat()}",
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
            if (state.movie.revenue == null) {
                Shimmer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(20.dp)
                )
            } else if (state.movie.revenue > 0) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Revenue",
                        style = MaterialTheme.typography.titleMedium,
                    )
                    Text(
                        text = "$${state.movie.revenue.toAmountFormat()}",
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun MovieDetailsScreenPreview() {
    MaterialTheme {
        MovieDetailsScreenContent(
            state = MovieDetailsUdf.State.Data(
                movie = Movie.mock
            ),
            onAction = {},
        )
    }
}