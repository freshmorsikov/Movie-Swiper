package com.github.freshmorsikov.moviematcher.feature.watchlists

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.github.freshmorsikov.moviematcher.core.data.api.IMAGE_BASE_URL
import com.github.freshmorsikov.moviematcher.core.ui.LoadingContent
import com.github.freshmorsikov.moviematcher.core.ui.MovieScaffold
import com.github.freshmorsikov.moviematcher.core.ui.paddingWithSystemTopBar
import com.github.freshmorsikov.moviematcher.core.ui.theme.MovieTheme
import com.github.freshmorsikov.moviematcher.feature.watchlists.domain.Watchlist
import com.github.freshmorsikov.moviematcher.feature.watchlists.domain.WatchlistType
import com.github.freshmorsikov.moviematcher.feature.watchlists.presentation.WatchlistsUdf
import com.github.freshmorsikov.moviematcher.feature.watchlists.presentation.WatchlistsViewModel
import com.github.freshmorsikov.moviematcher.shared.domain.model.Movie
import moviematcher.composeapp.generated.resources.Res
import moviematcher.composeapp.generated.resources.watchlist_disliked
import moviematcher.composeapp.generated.resources.watchlist_liked
import moviematcher.composeapp.generated.resources.watchlist_matches
import moviematcher.composeapp.generated.resources.watchlist_movie_count
import moviematcher.composeapp.generated.resources.watchlist_watched
import moviematcher.composeapp.generated.resources.watchlists_title
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun WatchlistsScreen(
    viewModel: WatchlistsViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    WatchlistsContent(state = state)
}

@Composable
private fun WatchlistsContent(
    state: WatchlistsUdf.State,
) {
    MovieScaffold {
        when (state) {
            WatchlistsUdf.State.Loading -> {
                LoadingContent(modifier = Modifier.fillMaxSize())
            }

            is WatchlistsUdf.State.Data -> {
                WatchlistsGrid(watchlists = state.watchlists)
            }
        }
    }
}

@Composable
private fun WatchlistsGrid(
    watchlists: List<Watchlist>,
    modifier: Modifier = Modifier,
) {
    LazyVerticalGrid(
        modifier = modifier.fillMaxSize(),
        columns = GridCells.Fixed(count = 2),
        contentPadding = paddingWithSystemTopBar(all = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        overscrollEffect = null,
    ) {
        item(span = { GridItemSpan(maxLineSpan) }) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp),
                text = stringResource(Res.string.watchlists_title),
                style = MovieTheme.typography.title20,
                color = MovieTheme.colors.text.main,
            )
        }
        items(
            items = watchlists,
            key = { watchlist -> watchlist.type.name },
        ) { watchlist ->
            WatchlistCard(watchlist = watchlist)
        }
    }
}

@Composable
private fun WatchlistCard(
    watchlist: Watchlist,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
        ),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, Color.Black.copy(alpha = 0.1f)),
    ) {
        Column {
            PosterPreview(
                posterPaths = watchlist.previewMovies.map { movie -> movie.posterPath },
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(ratio = 1f),
            )
            Column(
                modifier = Modifier.padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(watchlist.type.titleResource()),
                    style = MovieTheme.typography.title16,
                    color = MovieTheme.colors.text.main,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = movieCountText(movieCount = watchlist.movieCount),
                    style = MovieTheme.typography.body14,
                    color = MovieTheme.colors.text.variant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

@Composable
private fun PosterPreview(
    posterPaths: List<String>,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(
        topStart = 8.dp,
        topEnd = 8.dp,
    )
    Box(
        modifier = modifier
            .clip(shape)
            .background(MovieTheme.colors.surface.variant),
    ) {
        when (posterPaths.size) {
            0 -> {
                EmptyPosterTile(modifier = Modifier.fillMaxSize())
            }

            1 -> {
                PosterTile(
                    posterPath = posterPaths.first(),
                    modifier = Modifier.fillMaxSize(),
                )
            }

            2 -> {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                ) {
                    posterPaths.forEach { posterPath ->
                        PosterTile(
                            posterPath = posterPath,
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight(),
                        )
                    }
                }
            }

            else -> {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                ) {
                    PosterTile(
                        posterPath = posterPaths[0],
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                    )
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        verticalArrangement = Arrangement.spacedBy(2.dp),
                    ) {
                        PosterTile(
                            posterPath = posterPaths[1],
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(),
                        )
                        PosterTile(
                            posterPath = posterPaths[2],
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(),
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PosterTile(
    posterPath: String,
    modifier: Modifier = Modifier,
) {
    if (posterPath.isEmpty()) {
        EmptyPosterTile(modifier = modifier)
    } else {
        AsyncImage(
            modifier = modifier.background(MovieTheme.colors.surface.variant),
            model = "$IMAGE_BASE_URL$posterPath",
            contentScale = ContentScale.Crop,
            contentDescription = null,
        )
    }
}

@Composable
private fun EmptyPosterTile(
    modifier: Modifier = Modifier,
) {
    Spacer(modifier = modifier.background(MovieTheme.colors.surface.variant))
}

@Composable
private fun movieCountText(movieCount: Int): String {
    return pluralStringResource(
        resource = Res.plurals.watchlist_movie_count,
        quantity = movieCount,
        movieCount,
    )
}

private fun WatchlistType.titleResource(): StringResource {
    return when (this) {
        WatchlistType.Liked -> Res.string.watchlist_liked
        WatchlistType.Disliked -> Res.string.watchlist_disliked
        WatchlistType.Matches -> Res.string.watchlist_matches
        WatchlistType.Watched -> Res.string.watchlist_watched
    }
}

@Preview
@Composable
private fun WatchlistsContentPreview() {
    MovieTheme {
        WatchlistsContent(
            state = WatchlistsUdf.State.Data(
                watchlists = listOf(
                    Watchlist(
                        type = WatchlistType.Liked,
                        movieCount = 1,
                        previewMovies = List(3) { Movie.mock },
                    ),
                    Watchlist(
                        type = WatchlistType.Disliked,
                        movieCount = 3,
                        previewMovies = List(2) { Movie.mock },
                    ),
                    Watchlist(
                        type = WatchlistType.Matches,
                        movieCount = 0,
                        previewMovies = emptyList(),
                    ),
                    Watchlist(
                        type = WatchlistType.Watched,
                        movieCount = 0,
                        previewMovies = emptyList(),
                    ),
                )
            )
        )
    }
}
