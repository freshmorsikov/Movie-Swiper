package com.github.freshmorsikov.moviematcher.feature.swipe

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
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
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

private val cardShape = RoundedCornerShape(8.dp)

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
                            onAction = onAction,
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
                onClick = {
                    onAction(SwipeUdf.Action.Dislike)
                }
            )
            MovieButton(
                modifier = Modifier.weight(1f),
                text = "Like",
                containerColor = Color(0xFF00BE64),
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
        shape = cardShape
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
    onAction: (SwipeUdf.Action) -> Unit,
    modifier: Modifier = Modifier,
) {
    MovieStack(
        modifier = modifier,
        swipe = state.swipe,
        top = state.movies.lastOrNull(),
        middle = state.movies.getOrNull(state.movies.lastIndex - 1),
        bottom = state.movies.getOrNull(state.movies.lastIndex - 2),
        new = state.movies.getOrNull(state.movies.lastIndex - 3),
        onAction = onAction,
    )
}

@Composable
private fun MovieStack(
    swipe: SwipeUdf.SwipeDirection?,
    top: Movie?,
    middle: Movie?,
    bottom: Movie?,
    new: Movie?,
    onAction: (SwipeUdf.Action) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.TopCenter,
    ) {
        val newScale = remember(swipe) { Animatable(0.7f) }
        val newOffsetDp = remember(swipe) { Animatable(-16f) }
        val newAlpha = remember(swipe) { Animatable(0f) }
        new?.let {
            MovieCard(
                modifier = Modifier.graphicsLayer {
                    alpha = newAlpha.value
                    translationY = newOffsetDp.value * density
                    scaleX = newScale.value
                    scaleY = newScale.value
                    transformOrigin = TransformOrigin(
                        pivotFractionX = 0.5f,
                        pivotFractionY = 0f,
                    )
                }.blur(
                    radius = 10.dp,
                    edgeTreatment = BlurredEdgeTreatment(shape = cardShape),
                ),
                movie = new,
            )
        }

        val bottomScale = remember(swipe) { Animatable(0.8f) }
        val bottomOffsetDp = remember(swipe) { Animatable(0f) }
        bottom?.let {
            MovieCard(
                modifier = Modifier.graphicsLayer {
                    translationY = bottomOffsetDp.value * density
                    scaleX = bottomScale.value
                    scaleY = bottomScale.value
                    transformOrigin = TransformOrigin(
                        pivotFractionX = 0.5f,
                        pivotFractionY = 0f,
                    )
                }.blur(
                    radius = 10.dp,
                    edgeTreatment = BlurredEdgeTreatment(shape = cardShape),
                ),
                movie = bottom,
            )
        }

        val middleScale = remember(swipe) { Animatable(0.9f) }
        val middleOffsetDp = remember(swipe) { Animatable(16f) }
        val middleBlur = remember(swipe) { Animatable(initialValue = 10f) }
        middle?.let {
            MovieCard(
                modifier = Modifier.graphicsLayer {
                    translationY = middleOffsetDp.value * density
                    scaleX = middleScale.value
                    scaleY = middleScale.value
                    transformOrigin = TransformOrigin(
                        pivotFractionX = 0.5f,
                        pivotFractionY = 0f,
                    )
                }.blur(
                    radius = middleBlur.value.dp,
                    edgeTreatment = BlurredEdgeTreatment(shape = cardShape),
                ),
                movie = middle,
            )
        }

        val topOffsetDp = remember(swipe) { Animatable(0f) }
        val topAlpha = remember(swipe) { Animatable(1f) }

        val scope = rememberCoroutineScope()
        fun startAnimations(swipe: SwipeUdf.SwipeDirection) {
            val animationSpec: AnimationSpec<Float> = tween(500)
            scope.launch {
                listOf(
                    launch {
                        val multiplier = if (swipe == SwipeUdf.SwipeDirection.Right) 1 else -1
                        topOffsetDp.animateTo(multiplier * 380f)
                    },
                    launch { topAlpha.animateTo(0f, animationSpec) },
                    launch { middleScale.animateTo(1f, animationSpec) },
                    launch { middleOffsetDp.animateTo(32f, animationSpec) },
                    launch { middleBlur.animateTo(0f, animationSpec) },
                    launch { bottomScale.animateTo(0.9f, animationSpec) },
                    launch { bottomOffsetDp.animateTo(16f, animationSpec) },
                    launch { newScale.animateTo(0.8f, animationSpec) },
                    launch { newOffsetDp.animateTo(0f, animationSpec) },
                    launch { newAlpha.animateTo(1f, animationSpec) },
                ).joinAll()
                onAction(SwipeUdf.Action.FinishSwiping)
            }
        }

        top?.let {
            MovieCard(
                modifier = Modifier.graphicsLayer {
                    translationY = 32 * density
                    translationX = topOffsetDp.value * density
                    alpha = topAlpha.value
                },
                movie = top,
            )
        }

        LaunchedEffect(swipe) {
            if (swipe != null) {
                startAnimations(swipe = swipe)
            }
        }
    }
}

@Composable
private fun MovieCard(
    movie: Movie,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp)
        ),
        shape = cardShape,
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
            swipe = SwipeUdf.SwipeDirection.Left,
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