package com.github.freshmorsikov.moviematcher.feature.swipe

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import com.github.freshmorsikov.moviematcher.core.ui.MovieButton
import com.github.freshmorsikov.moviematcher.core.ui.MovieScaffold
import com.github.freshmorsikov.moviematcher.feature.swipe.domain.model.Movie
import com.github.freshmorsikov.moviematcher.feature.swipe.presentation.SwipeUdf
import com.github.freshmorsikov.moviematcher.feature.swipe.presentation.SwipeViewModel
import com.github.freshmorsikov.moviematcher.shared.ui.movie.MovieGenres
import com.github.freshmorsikov.moviematcher.shared.ui.movie.MovieInfo
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
        Box(modifier = Modifier.padding(16.dp)) {
            Box(
                modifier = Modifier
                    .padding(bottom = 72.dp)
                    .align(Alignment.Center),
            ) {
                when (state) {
                    SwipeUdf.State.Loading -> {
                        LoadingContent(modifier = Modifier.align(Alignment.Center))
                    }

                    is SwipeUdf.State.Data -> {
                        DataContent(
                            modifier = Modifier.align(Alignment.Center),
                            state = state,
                        )
                    }
                }
            }
        }

        Row(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.BottomCenter),
            horizontalArrangement = spacedBy(8.dp)
        ) {
            MovieButton(
                modifier = Modifier.weight(1f),
                text = "Dislike",
                containerColor = Color(0xFFF95667),
                //enabled = state.isButtonsEnabled,
                onClick = {
                    onAction(SwipeUdf.Action.Dislike)
                }
            )
            MovieButton(
                modifier = Modifier.weight(1f),
                text = "Like",
                containerColor = Color(0xFF00BE64),
                //enabled = state.isButtonsEnabled,
                onClick = {
                    onAction(SwipeUdf.Action.Like)
                }
            )
        }
    }
}

@Composable
private fun LoadingContent(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(600.dp)
            .containerShimmer(),
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp),
            verticalArrangement = spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(
                modifier = Modifier
                    .height(24.dp)
                    .width(120.dp)
                    .contentShimmer()
            )
            Spacer(
                modifier = Modifier
                    .height(32.dp)
                    .fillMaxWidth()
                    .contentShimmer()
            )
            Spacer(
                modifier = Modifier
                    .height(32.dp)
                    .fillMaxWidth()
                    .contentShimmer()
            )
        }
    }
}

@Composable
private fun Modifier.containerShimmer(): Modifier {
    val transition = rememberInfiniteTransition(label = "containerTransition")
    val alpha by transition.animateFloat(
        initialValue = 0.5f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(800),
            repeatMode = RepeatMode.Reverse
        ),
        label = "containerAnimation"
    )
    return background(
        color = MaterialTheme.colorScheme.surfaceContainer.copy(alpha = alpha),
        shape = RoundedCornerShape(8.dp)
    )
}

@Composable
private fun Modifier.contentShimmer(): Modifier {
    val transition = rememberInfiniteTransition(label = "contentTransition")
    val alpha by transition.animateFloat(
        initialValue = 0.5f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(800),
            repeatMode = RepeatMode.Reverse
        ),
        label = "contentAnimation"
    )
    return background(
        color = MaterialTheme.colorScheme.surfaceContainerHighest.copy(alpha = alpha),
        shape = RoundedCornerShape(8.dp)
    )
}

@Composable
private fun DataContent(
    state: SwipeUdf.State.Data,
    modifier: Modifier = Modifier,
) {
    MovieCard(
        state = state,
        modifier = modifier,
    )
}

@Composable
private fun MovieCard(
    state: SwipeUdf.State.Data,
    modifier: Modifier = Modifier,
) {
    val currentMovie = state.movies.firstOrNull() ?: return
    AnimatedContent(
        targetState = currentMovie,
        transitionSpec = {
            ContentTransform(
                targetContentEnter = EnterTransition.None,
                initialContentExit = fadeOut(tween(durationMillis = 500)) + slideOutHorizontally { fullWidth ->
                    if (state.swipeDirection == SwipeUdf.SwipeDirection.Left) {
                        -fullWidth
                    } else {
                        fullWidth
                    }
                },
                targetContentZIndex = state.zIndex,
            )
        },
        label = "movieCardAnimation",
    ) { movie ->
        Card(
            modifier = modifier,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp)
            ),
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(0.5.dp, Color.Black.copy(alpha = 0.1f))
        ) {
            Column {
                AsyncImage(
                    modifier = Modifier
                        .height(500.dp)
                        .width(380.dp),
                    model = "$IMAGE_BASE_URL${movie.posterPath}",
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
                        text = movie.title,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center,
                    )
                    MovieInfo(
                        releaseDate = movie.releaseDate,
                        voteAverage = movie.voteAverage,
                    )
                    MovieGenres(movie.genres)
                }
            }
        }
    }
}

@Preview
@Composable
private fun SwipeScreenDataPreview() {
    SwipeScreenContent(
        state = SwipeUdf.State.Data(
            movies = List(3) { i ->
                Movie(
                    id = i.toLong(),
                    title = "Title $i",
                    originalTitle = "Original title",
                    posterPath = "path",
                    releaseDate = "2025",
                    voteAverage = 2.4,
                    popularity = 70.0,
                    status = "",
                    genres = listOf("Comedy", "Drama"),
                )
            },
            swipeDirection = SwipeUdf.SwipeDirection.Left,
            zIndex = 0f,
            isSwiping = false,
        ),
        onAction = {}
    )
}

@Preview
@Composable
private fun SwipeScreenLoadingPreview() {
    SwipeScreenContent(
        state = SwipeUdf.State.Loading,
        onAction = {}
    )
}