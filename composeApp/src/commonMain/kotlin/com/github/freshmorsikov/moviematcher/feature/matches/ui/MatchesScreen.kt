package com.github.freshmorsikov.moviematcher.feature.matches.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import com.github.freshmorsikov.moviematcher.core.ui.LoadingContent
import com.github.freshmorsikov.moviematcher.core.ui.MovieScaffold
import com.github.freshmorsikov.moviematcher.core.ui.paddingWithSystemTopBar
import com.github.freshmorsikov.moviematcher.core.ui.theme.MovieTheme
import com.github.freshmorsikov.moviematcher.feature.matches.presentation.MatchesUdf
import com.github.freshmorsikov.moviematcher.feature.matches.presentation.MatchesViewModel
import com.github.freshmorsikov.moviematcher.shared.domain.model.Movie
import com.github.freshmorsikov.moviematcher.shared.ui.UserPairCard
import com.github.freshmorsikov.moviematcher.shared.ui.UserPairState
import com.github.freshmorsikov.moviematcher.shared.ui.movie.MovieItem
import moviematcher.composeapp.generated.resources.Res
import moviematcher.composeapp.generated.resources.matches_info_text
import moviematcher.composeapp.generated.resources.matches_info_title
import moviematcher.composeapp.generated.resources.popcorny_like
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
                LoadingContent(modifier = Modifier.fillMaxSize())
            }

            is MatchesUdf.State.Empty -> {
                MatchesInfo()
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
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = paddingWithSystemTopBar(all = 16.dp),
        verticalArrangement = spacedBy(8.dp),
        overscrollEffect = null,
    ) {
        item {
            PairUserCardItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )
        }
        items(movies) { movie ->
            MovieItem(
                movie = movie,
                onClick = onMovieClick,
            )
        }
    }
}

@Composable
private fun MatchesInfo(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(paddingWithSystemTopBar(all = 16.dp)),
    ) {
        PairUserCardItem(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                modifier = Modifier.width(width = 360.dp),
                painter = painterResource(Res.drawable.popcorny_like),
                contentDescription = null,
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
                text = stringResource(Res.string.matches_info_title),
                style = MovieTheme.typography.title16,
                color = MovieTheme.colors.text.variant,
                textAlign = TextAlign.Center,
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                text = stringResource(Res.string.matches_info_text),
                style = MovieTheme.typography.body14,
                color = MovieTheme.colors.text.variant,
                textAlign = TextAlign.Center,
            )
        }
        }
    }
}

@Composable
private fun PairUserCardItem(modifier: Modifier = Modifier) {
    UserPairCard(
        modifier = modifier,
        onInviteClick = {},
        state = UserPairState.Paired(
            userName = "Alan",
            friendName = "Kate",
            userEmoji = "\uD83D\uDC35",
            friendEmoji = "\uD83D\uDC36",
        ),
    )
}

@Preview
@Composable
private fun PairedPreview(
    @PreviewParameter(MatchesStateProvider::class) state: MatchesUdf.State
) {
    MovieTheme {
        MatchesContent(
            state = state,
            onMovieClick = {},
        )
    }
}
