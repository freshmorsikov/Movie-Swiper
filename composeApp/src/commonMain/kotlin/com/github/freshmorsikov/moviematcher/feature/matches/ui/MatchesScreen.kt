package com.github.freshmorsikov.moviematcher.feature.matches.ui

import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.github.freshmorsikov.moviematcher.app.NavigationRoute
import com.github.freshmorsikov.moviematcher.core.ui.MovieScaffold
import com.github.freshmorsikov.moviematcher.feature.matches.presentation.MatchesUdf
import com.github.freshmorsikov.moviematcher.feature.matches.presentation.MatchesViewModel
import com.github.freshmorsikov.moviematcher.shared.domain.model.Movie
import com.github.freshmorsikov.moviematcher.shared.ui.movie.MovieItem
import moviematcher.composeapp.generated.resources.Res
import moviematcher.composeapp.generated.resources.ic_match
import moviematcher.composeapp.generated.resources.matches_empty
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jetbrains.compose.ui.tooling.preview.PreviewParameter
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MatchesScreen(
    navController: NavController,
    viewModel: MatchesViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    MatchesContent(
        state = state,
        onMovieClick = { movieId ->
            navController.navigate(
                NavigationRoute.MovieDetails(movieId = movieId)
            )
        }
    )
}

@Composable
private fun MatchesContent(
    state: MatchesUdf.State,
    onMovieClick: (Long) -> Unit,
) {
    MovieScaffold {
        when (state) {
            MatchesUdf.State.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(40.dp),
                    color = MaterialTheme.colorScheme.secondary
                )
            }

            is MatchesUdf.State.Empty -> {
                MatchesInfo(
                    modifier = Modifier.align(Alignment.Center),
                    text = stringResource(Res.string.matches_empty)
                )
            }

            is MatchesUdf.State.Data -> {
                MatchesListContent(
                    movies = state.movies,
                    onMovieClick = onMovieClick,
                )
            }
        }
    }
}

@Composable
private fun MatchesListContent(
    movies: List<Movie>,
    onMovieClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = spacedBy(8.dp)
        ) {
            items(movies) { movie ->
                MovieItem(
                    movie = movie,
                    onClick = onMovieClick,
                )
            }
        }
    }
}

@Composable
private fun MatchesInfo(
    text: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            modifier = Modifier.size(120.dp),
            painter = painterResource(Res.drawable.ic_match),
            tint = MaterialTheme.colorScheme.secondary,
            contentDescription = null
        )
        Text(
            modifier = Modifier
                .padding(top = 24.dp)
                .fillMaxWidth(),
            text = text,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.secondary,
            textAlign = TextAlign.Center,
        )
    }
}

@Preview
@Composable
private fun PairedPreview(
    @PreviewParameter(MatchesStateProvider::class) state: MatchesUdf.State
) {
    MaterialTheme {
        MatchesContent(
            state = state,
            onMovieClick = {},
        )
    }
}