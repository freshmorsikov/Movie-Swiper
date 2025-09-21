package com.github.freshmorsikov.moviematcher.feature.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
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
import moviematcher.composeapp.generated.resources.Res
import moviematcher.composeapp.generated.resources.ic_back
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun MovieDetailsScreen(
    movieId: Long,
    navController: NavController,
    viewModel: MovieDetailsViewModel = koinViewModel(
        parameters = { parametersOf(movieId) }
    )
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    MovieDetailsScreenContent(
        state = state,
        onAction = viewModel::onAction,
        onBackClick = {
            navController.popBackStack()
        },
    )
}

@Composable
private fun MovieDetailsScreenContent(
    state: MovieDetailsUdf.State,
    onAction: (MovieDetailsUdf.Action) -> Unit,
    onBackClick: () -> Unit,
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
        FilledIconButton(
            modifier = Modifier
                .padding(top = WindowInsets.systemBars.asPaddingValues().calculateTopPadding() + 8.dp)
                .padding(start = 16.dp)
                .size(48.dp),
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = Color.White.copy(alpha = 0.3f)
            ),
            onClick = onBackClick
        ) {
            Icon(
                modifier = Modifier.size(24.dp),
                painter = painterResource(Res.drawable.ic_back),
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                contentDescription = null
            )
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
    Column(
        modifier = Modifier.verticalScroll(
            state = rememberScrollState()
        )
    ) {
        AsyncImage(
            modifier = Modifier.fillMaxWidth(),
            model = "$IMAGE_BASE_URL${state.movie.posterPath}",
            contentScale = ContentScale.FillWidth,
            contentDescription = null,
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
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

            Spacer(modifier = Modifier.height(8.dp))

            if (state.movie.overview == null) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Shimmer(
                        modifier = Modifier
                            .width(120.dp)
                            .height(20.dp)
                    )
                    Shimmer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(20.dp)
                    )
                }
            } else {
                Column {
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
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Shimmer(
                        modifier = Modifier
                            .width(120.dp)
                            .height(20.dp)
                    )
                    Shimmer(
                        modifier = Modifier
                            .width(160.dp)
                            .height(20.dp)
                    )
                }
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
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Shimmer(
                        modifier = Modifier
                            .width(120.dp)
                            .height(20.dp)
                    )
                    Shimmer(
                        modifier = Modifier
                            .width(160.dp)
                            .height(20.dp)
                    )
                }
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
            onBackClick = {},
        )
    }
}