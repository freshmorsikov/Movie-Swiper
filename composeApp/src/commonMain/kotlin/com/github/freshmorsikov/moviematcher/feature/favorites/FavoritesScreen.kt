package com.github.freshmorsikov.moviematcher.feature.favorites

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.github.freshmorsikov.moviematcher.core.data.api.IMAGE_BASE_URL
import com.github.freshmorsikov.moviematcher.core.ui.MovieScaffold
import com.github.freshmorsikov.moviematcher.feature.favorites.presentation.FavoritesUdf
import com.github.freshmorsikov.moviematcher.feature.favorites.presentation.FavoritesViewModel
import com.github.freshmorsikov.moviematcher.feature.swipe.domain.model.Movie
import com.github.freshmorsikov.moviematcher.shared.ui.movie.MovieGenres
import com.github.freshmorsikov.moviematcher.shared.ui.movie.MovieInfo
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
                        FavoriteCard(movie = movie)
                    }
                }
            }
        }
    }
}

@Composable
private fun FavoriteCard(
    movie: Movie,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp)
        ),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(0.5.dp, Color.Black.copy(alpha = 0.1f))
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
                )
                MovieGenres(genres = movie.genres)
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
                movieList = List(10) { i ->
                    Movie(
                        id = i.toLong(),
                        title = "Title $i",
                        originalTitle = "",
                        posterPath = "",
                        releaseDate = "2024-11-11",
                        voteAverage = 2.9090,
                        popularity = 0.0,
                        status = "",
                        genres = listOf("Comedy", "Drama", "Horror"),
                    )
                }
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