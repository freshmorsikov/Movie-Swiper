package com.github.freshmorsikov.moviematcher.feature.details

import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.github.freshmorsikov.moviematcher.core.data.api.IMAGE_BASE_URL
import com.github.freshmorsikov.moviematcher.core.ui.MovieScaffold
import com.github.freshmorsikov.moviematcher.core.ui.Shimmer
import com.github.freshmorsikov.moviematcher.core.ui.theme.MovieTheme
import com.github.freshmorsikov.moviematcher.feature.details.domain.model.Actor
import com.github.freshmorsikov.moviematcher.feature.details.presentation.MovieDetailsUdf
import com.github.freshmorsikov.moviematcher.feature.details.presentation.MovieDetailsViewModel
import com.github.freshmorsikov.moviematcher.shared.domain.model.Movie
import com.github.freshmorsikov.moviematcher.shared.ui.movie.MovieGenres
import com.github.freshmorsikov.moviematcher.shared.ui.movie.MovieInfo
import com.github.freshmorsikov.moviematcher.shared.ui.player.MoviePlayerView
import com.github.freshmorsikov.moviematcher.shared.ui.player.PlayerContent
import com.github.freshmorsikov.moviematcher.shared.ui.player.PlayerStateController
import com.github.freshmorsikov.moviematcher.util.toAmountFormat
import moviematcher.composeapp.generated.resources.Res
import moviematcher.composeapp.generated.resources.ic_back
import moviematcher.composeapp.generated.resources.movie_details_budget
import moviematcher.composeapp.generated.resources.movie_details_cast
import moviematcher.composeapp.generated.resources.movie_details_overview
import moviematcher.composeapp.generated.resources.movie_details_revenue
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject
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
        onBackClick = {
            navController.popBackStack()
        },
    )
}

@Composable
private fun MovieDetailsScreenContent(
    state: MovieDetailsUdf.State,
    onBackClick: () -> Unit,
) {
    MovieScaffold(background = MovieTheme.colors.surface.main) {
        when (state) {
            MovieDetailsUdf.State.Loading -> {
                LoadingMovieDetailsScreenContent()
            }

            is MovieDetailsUdf.State.Data -> {
                LoadedMovieDetailsScreenContent(
                    modifier = Modifier
                        .verticalScroll(
                            state = rememberScrollState(),
                            overscrollEffect = null,
                        )
                        .padding(
                            bottom = WindowInsets.systemBars.asPaddingValues().calculateBottomPadding()
                        ),
                    state = state,
                )
            }
        }
        FilledIconButton(
            modifier = Modifier
                .padding(top = WindowInsets.systemBars.asPaddingValues().calculateTopPadding() + 8.dp)
                .padding(start = 16.dp)
                .size(48.dp)
                .border(
                    width = 1.dp,
                    color = MovieTheme.colors.surface.main.copy(alpha = 0.8f),
                    shape = CircleShape
                ),
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = MovieTheme.colors.surface.main.copy(
                    alpha = 0.6f
                )
            ),
            onClick = onBackClick
        ) {
            Icon(
                modifier = Modifier.size(24.dp),
                painter = painterResource(Res.drawable.ic_back),
                tint = MovieTheme.colors.icon.main,
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
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        AsyncImage(
            modifier = Modifier.fillMaxWidth(),
            model = "$IMAGE_BASE_URL${state.movie.posterPath}",
            contentScale = ContentScale.FillWidth,
            contentDescription = null,
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            verticalArrangement = spacedBy(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = state.movie.title,
                    color = MovieTheme.colors.text.main,
                    style = MovieTheme.typography.title20,
                    textAlign = TextAlign.Center,
                )
                MovieInfo(
                    releaseDate = state.movie.releaseDate,
                    voteAverage = state.movie.voteAverage,
                    voteCount = state.movie.voteCount,
                    runtime = state.movie.runtime,
                )
                MovieGenres(state.movie.genres)
            }
            OverviewBlock(
                modifier = Modifier.padding(horizontal = 16.dp),
                overview = state.movie.overview,
            )

            val playerController = koinInject<PlayerStateController>()
            LaunchedEffect(Unit) {
                playerController.init {}
                playerController.setContent(
                    PlayerContent(
                        name = state.movie.title,
                        url = "https://youtu.be/Mzw2ttJD2qQ"
                    )
                )
                playerController.play()
            }
            MoviePlayerView(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .padding(horizontal = 16.dp),
                controller = playerController,
            )

            CastBlock(actors = state.actors)
            BudgetBlock(
                modifier = Modifier.padding(horizontal = 16.dp),
                budget = state.movie.budget,
            )
            RevenueBlock(
                modifier = Modifier.padding(horizontal = 16.dp),
                revenue = state.movie.revenue,
            )
        }
    }
}

@Composable
private fun OverviewBlock(
    modifier: Modifier = Modifier,
    overview: String?,
) {
    if (overview == null) {
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
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
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(Res.string.movie_details_overview),
                color = MovieTheme.colors.text.main,
                style = MovieTheme.typography.title16,
            )
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = overview,
                color = MovieTheme.colors.text.main,
                style = MovieTheme.typography.body14,
            )
        }
    }
}

@Composable
private fun CastBlock(
    modifier: Modifier = Modifier,
    actors: List<Actor>?,
) {
    if (actors == null) {
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Shimmer(
                modifier = Modifier
                    .width(120.dp)
                    .height(20.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = spacedBy(8.dp)
            ) {
                repeat(8) {
                    Column(verticalArrangement = spacedBy(4.dp)) {
                        Shimmer(
                            modifier = Modifier
                                .size(96.dp)
                                .clip(shape = RoundedCornerShape(8.dp))
                        )
                        Shimmer(
                            modifier = Modifier
                                .width(80.dp)
                                .height(16.dp)
                        )
                        Shimmer(
                            modifier = Modifier
                                .width(60.dp)
                                .height(16.dp)
                        )
                    }
                }
            }
        }
    } else if (actors.isNotEmpty()) {
        Column(
            modifier = modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = stringResource(Res.string.movie_details_cast),
                color = MovieTheme.colors.text.main,
                style = MovieTheme.typography.title16,
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp),
                horizontalArrangement = spacedBy(8.dp)
            ) {
                actors.forEach { actor ->
                    Column(modifier = Modifier.width(120.dp)) {
                        AsyncImage(
                            modifier = Modifier
                                .size(120.dp)
                                .clip(shape = RoundedCornerShape(8.dp)),
                            model = "$IMAGE_BASE_URL${actor.profilePath}",
                            contentScale = ContentScale.FillWidth,
                            contentDescription = null,
                        )
                        Text(
                            modifier = Modifier.padding(top = 4.dp),
                            text = actor.name,
                            color = MovieTheme.colors.text.main,
                            style = MovieTheme.typography.body14,
                        )
                        Text(
                            modifier = Modifier.padding(top = 2.dp),
                            text = actor.character,
                            color = MovieTheme.colors.text.variant,
                            style = MovieTheme.typography.body12,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun BudgetBlock(
    modifier: Modifier = Modifier,
    budget: Long?,
) {
    if (budget == null) {
        Column(
            modifier = modifier.fillMaxWidth(),
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
    } else if (budget > 0) {
        Column(
            modifier = modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = stringResource(Res.string.movie_details_budget),
                color = MovieTheme.colors.text.main,
                style = MovieTheme.typography.title16,
            )
            Text(
                text = "$${budget.toAmountFormat()}",
                color = MovieTheme.colors.text.main,
                style = MovieTheme.typography.body14,
            )
        }
    }
}

@Composable
private fun RevenueBlock(
    modifier: Modifier = Modifier,
    revenue: Long?,
) {
    if (revenue == null) {
        Column(
            modifier = modifier.fillMaxWidth(),
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
    } else if (revenue > 0) {
        Column(
            modifier = modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = stringResource(Res.string.movie_details_revenue),
                color = MovieTheme.colors.text.main,
                style = MovieTheme.typography.title16,
            )
            Text(
                text = "$${revenue.toAmountFormat()}",
                color = MovieTheme.colors.text.main,
                style = MovieTheme.typography.body14,
            )
        }
    }
}

@Preview
@Composable
private fun MovieDetailsScreenPreview() {
    MovieTheme {
        MovieDetailsScreenContent(
            state = MovieDetailsUdf.State.Data(
                movie = Movie.mock,
                actors = List(5) {
                    Actor.mock
                }
            ),
            onBackClick = {},
        )
    }
}