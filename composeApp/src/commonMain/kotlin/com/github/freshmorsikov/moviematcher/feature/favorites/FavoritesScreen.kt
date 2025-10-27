package com.github.freshmorsikov.moviematcher.feature.favorites

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.github.freshmorsikov.moviematcher.app.navigation.NavigationRoute
import com.github.freshmorsikov.moviematcher.core.ui.MovieScaffold
import com.github.freshmorsikov.moviematcher.core.ui.theme.MovieTheme
import com.github.freshmorsikov.moviematcher.feature.favorites.presentation.FavoritesUdf
import com.github.freshmorsikov.moviematcher.feature.favorites.presentation.FavoritesViewModel
import com.github.freshmorsikov.moviematcher.shared.domain.model.Movie
import com.github.freshmorsikov.moviematcher.shared.ui.movie.MovieItem
import moviematcher.composeapp.generated.resources.Res
import moviematcher.composeapp.generated.resources.favorites_do_you_have
import moviematcher.composeapp.generated.resources.favorites_your_favorite_movies
import moviematcher.composeapp.generated.resources.popcorny_puzzled
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun FavoriteScreen(
    navController: NavController,
    viewModel: FavoritesViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    FavoriteScreenContent(
        state = state,
        onMovieClick = { movieId ->
            navController.navigate(
                NavigationRoute.MovieDetails(movieId = movieId)
            )
        },
    )
}

@Composable
fun FavoriteScreenContent(
    state: FavoritesUdf.State,
    onMovieClick: (Long) -> Unit,
) {
    MovieScaffold {
        when (state) {
            FavoritesUdf.State.Loading -> {}
            FavoritesUdf.State.Empty -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        modifier = Modifier.width(width = 360.dp),
                        painter = painterResource(Res.drawable.popcorny_puzzled),
                        contentDescription = null,
                    )
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        text = stringResource(Res.string.favorites_your_favorite_movies),
                        style = MovieTheme.typography.title16,
                        color = MovieTheme.colors.text.variant,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        text = stringResource(Res.string.favorites_do_you_have),
                        style = MovieTheme.typography.title16,
                        color = MovieTheme.colors.text.variant,
                        textAlign = TextAlign.Center
                    )
                }
            }

            is FavoritesUdf.State.Data -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = spacedBy(8.dp)
                ) {
                    items(state.movieList) { movie ->
                        MovieItem(
                            movie = movie,
                            onClick = onMovieClick,
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun FavoriteScreenContentPreview() {
    MovieTheme {
        FavoriteScreenContent(
            state = FavoritesUdf.State.Data(
                movieList = List(6) { Movie.mock }
            ),
            onMovieClick = {},
        )
    }
}

@Preview
@Composable
private fun FavoriteScreenEmptyPreview() {
    MovieTheme {
        FavoriteScreenContent(
            state = FavoritesUdf.State.Empty,
            onMovieClick = {},
        )
    }
}