package com.github.freshmorsikov.moviematcher.feature.favorites

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.github.freshmorsikov.moviematcher.core.ui.MovieScaffold
import com.github.freshmorsikov.moviematcher.feature.favorites.presentation.FavoritesUdf
import com.github.freshmorsikov.moviematcher.feature.favorites.presentation.FavoritesViewModel
import com.github.freshmorsikov.moviematcher.feature.swipe.domain.model.Movie
import com.github.freshmorsikov.moviematcher.shared.ui.movie.MovieItem
import moviematcher.composeapp.generated.resources.Res
import moviematcher.composeapp.generated.resources.favorites_empty
import moviematcher.composeapp.generated.resources.ic_heart
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun FavoriteScreen(
    viewModel: FavoritesViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    FavoriteScreenContent(state = state)
}

@Composable
fun FavoriteScreenContent(state: FavoritesUdf.State) {
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
                    Icon(
                        modifier = Modifier.size(120.dp),
                        painter = painterResource(Res.drawable.ic_heart),
                        tint = MaterialTheme.colorScheme.secondary,
                        contentDescription = null
                    )
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 20.dp),
                        text = stringResource(Res.string.favorites_empty),
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.secondary,
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
                        MovieItem(movie = movie)
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun FavoriteScreenContentPreview() {
    MaterialTheme {
        FavoriteScreenContent(
            state = FavoritesUdf.State.Data(
                movieList = List(6) { Movie.mock }
            )
        )
    }
}

@Preview
@Composable
private fun FavoriteScreenEmptyPreview() {
    MaterialTheme {
        FavoriteScreenContent(
            state = FavoritesUdf.State.Empty
        )
    }
}