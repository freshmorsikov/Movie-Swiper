package com.github.freshmorsikov.moviematcher.feature.swipe

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.github.freshmorsikov.moviematcher.app.navigation.NavigationRoute
import com.github.freshmorsikov.moviematcher.core.data.api.IMAGE_BASE_URL
import com.github.freshmorsikov.moviematcher.core.ui.ContainerShimmer
import com.github.freshmorsikov.moviematcher.core.ui.MovieScaffold
import com.github.freshmorsikov.moviematcher.core.ui.Shimmer
import com.github.freshmorsikov.moviematcher.core.ui.none
import com.github.freshmorsikov.moviematcher.core.ui.theme.MovieTheme
import com.github.freshmorsikov.moviematcher.feature.swipe.presentation.SwipeUdf
import com.github.freshmorsikov.moviematcher.feature.swipe.presentation.SwipeUdf.MovieCardState
import com.github.freshmorsikov.moviematcher.feature.swipe.presentation.SwipeViewModel
import com.github.freshmorsikov.moviematcher.feature.swipe.ui.ColorIndicators
import com.github.freshmorsikov.moviematcher.shared.domain.model.Movie
import com.github.freshmorsikov.moviematcher.shared.ui.movie.MovieGenres
import com.github.freshmorsikov.moviematcher.shared.ui.movie.MovieInfo
import com.github.freshmorsikov.moviematcher.util.SharingManager
import com.github.freshmorsikov.moviematcher.util.SubscribeOnEvents
import com.github.freshmorsikov.moviematcher.util.clickableWithoutIndication
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import moviematcher.composeapp.generated.resources.Res
import moviematcher.composeapp.generated.resources.ic_chevron_right
import moviematcher.composeapp.generated.resources.sharing_message
import moviematcher.composeapp.generated.resources.sharing_title
import moviematcher.composeapp.generated.resources.swipe_create_pair
import moviematcher.composeapp.generated.resources.swipe_invite
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

private val cardShape = RoundedCornerShape(8.dp)
private val swipeAnimationSpec: AnimationSpec<Float> = tween(500)

@Composable
fun SwipeScreen(
    navController: NavController,
    viewModel: SwipeViewModel = koinViewModel(),
    sharingManager: SharingManager = koinInject(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    SwipeScreenContent(
        state = state,
        onAction = viewModel::onAction,
        onMovieClick = { movieId ->
            navController.navigate(
                NavigationRoute.MovieDetails(movieId = movieId)
            )
        }
    )

    val sharingTitle = stringResource(Res.string.sharing_title)
    val sharingMessage = stringResource(Res.string.sharing_message)
    SubscribeOnEvents(viewModel.event) { event ->
        when (event) {
            is SwipeUdf.Event.ShowSharingDialog -> {
                sharingManager.share(
                    title = sharingTitle,
                    text = "$sharingMessage ${event.inviteLink}"
                )
            }
        }
    }
}

@Composable
fun SwipeScreenContent(
    state: SwipeUdf.State,
    onAction: (SwipeUdf.Action) -> Unit,
    onMovieClick: (Long) -> Unit,
) {
    MovieScaffold(contentWindowInsets = WindowInsets.none) {
        Column {
            InviteBanner(
                visible = state.inviteBannerVisible,
                onAction = onAction,
            )
            MoviesBlock(
                movies = state.movies,
                onAction = onAction,
                onMovieClick = onMovieClick,
            )
        }
    }
}

@Composable
private fun InviteBanner(
    visible: Boolean,
    onAction: (SwipeUdf.Action) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.animateContentSize()) {
        val backgroundAlpha by animateFloatAsState(
            if (visible) 1f else 0f
        )
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    MovieTheme.colors.warning.copy(
                        alpha = backgroundAlpha
                    )
                ).padding(
                    top = WindowInsets.systemBars.asPaddingValues().calculateTopPadding(),
                )
        )
        AnimatedVisibility(
            modifier = modifier,
            visible = visible,
            enter = slideInVertically(
                animationSpec = tween(500)
            ) { fullHeight ->
                -fullHeight
            },
            exit = slideOutVertically(
                animationSpec = tween(500)
            ) { fullHeight ->
                -fullHeight
            },
        ) {
            Column(
                modifier = Modifier.background(
                    color = MovieTheme.colors.warning.copy(
                        alpha = backgroundAlpha
                    )
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                            onClick = {
                                onAction(SwipeUdf.Action.InviteClick)
                            },
                            interactionSource = null,
                            indication = null,
                        )
                        .padding(
                            horizontal = 16.dp,
                            vertical = 8.dp,
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = stringResource(Res.string.swipe_create_pair),
                        style = MovieTheme.typography.body14,
                        color = MovieTheme.colors.text.onWarning,
                    )
                    Text(
                        modifier = Modifier.padding(start = 8.dp),
                        text = stringResource(Res.string.swipe_invite),
                        style = MovieTheme.typography.label12,
                        color = MovieTheme.colors.text.onWarning,
                    )
                    Icon(
                        modifier = Modifier
                            .padding(start = 4.dp)
                            .size(16.dp),
                        painter = painterResource(Res.drawable.ic_chevron_right),
                        tint = MovieTheme.colors.text.onWarning,
                        contentDescription = null
                    )
                }
                HorizontalDivider(
                    thickness = 0.5.dp,
                    color = MovieTheme.colors.stroke,
                )
            }
        }
    }
}

@Composable
private fun MoviesBlock(
    movies: List<Movie>?,
    onAction: (SwipeUdf.Action) -> Unit,
    onMovieClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (movies == null) {
        LoadingContent(
            modifier = modifier.padding(16.dp)
        )
    } else {
        DataContent(
            modifier = modifier.fillMaxSize(),
            movies = movies,
            onAction = onAction,
            onMovieClick = onMovieClick,
        )
    }
}

@Composable
private fun LoadingContent(modifier: Modifier = Modifier) {
    ContainerShimmer(
        modifier = modifier
            .fillMaxWidth()
            .height(600.dp),
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp),
            verticalArrangement = spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Shimmer(
                modifier = Modifier
                    .height(24.dp)
                    .width(120.dp)
            )
            Shimmer(
                modifier = Modifier
                    .height(32.dp)
                    .fillMaxWidth(fraction = 0.4f)
            )
            Shimmer(
                modifier = Modifier
                    .height(32.dp)
                    .fillMaxWidth()
            )
        }
    }
}

@Composable
private fun DataContent(
    movies: List<Movie>,
    onAction: (SwipeUdf.Action) -> Unit,
    onMovieClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        val top = movies.lastOrNull()
        val windowInfo = LocalWindowInfo.current
        val draggableState = remember(top) {
            val width = windowInfo.containerSize.width.toFloat()
            AnchoredDraggableState<MovieCardState>(
                MovieCardState.Center,
                DraggableAnchors {
                    MovieCardState.Swiped.Left at -width
                    MovieCardState.Center at 0f
                    MovieCardState.Swiped.Right at width
                }
            )
        }
        val scrollState = rememberScrollState()
        MovieStack(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(top = 16.dp)
                .padding(horizontal = 16.dp)
                .padding(bottom = 48.dp),
            top = top,
            middle = movies.getOrNull(movies.lastIndex - 1),
            bottom = movies.getOrNull(movies.lastIndex - 2),
            new = movies.getOrNull(movies.lastIndex - 3),
            draggableState = draggableState,
            onAction = onAction,
            onMovieClick = onMovieClick,
        )
        ColorIndicators(
            modifier = Modifier.fillMaxSize(),
            draggableState = draggableState
        )
    }
}

@Composable
private fun MovieStack(
    top: Movie?,
    middle: Movie?,
    bottom: Movie?,
    new: Movie?,
    draggableState: AnchoredDraggableState<MovieCardState>,
    onAction: (SwipeUdf.Action) -> Unit,
    onMovieClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.anchoredDraggable(
            state = draggableState,
            orientation = Orientation.Horizontal,
        ),
        contentAlignment = Alignment.TopCenter,
    ) {
        val newScale = remember(new) { Animatable(0.7f) }
        val newOffsetDp = remember(new) { Animatable(-16f) }
        val newAlpha = remember(new) { Animatable(0f) }
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

        val bottomScale = remember(bottom) { Animatable(0.8f) }
        val bottomOffsetDp = remember(bottom) { Animatable(0f) }
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

        val middleScale = remember(middle) { Animatable(0.9f) }
        val middleOffsetDp = remember(middle) { Animatable(16f) }
        val middleBlur = remember(middle) { Animatable(initialValue = 10f) }
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

        val topAlpha = remember(top) { Animatable(1f) }
        val scope = rememberCoroutineScope()

        fun startAnimations(movieCardState: MovieCardState.Swiped) {
            scope.launch {
                listOf(
                    launch { topAlpha.animateTo(0f, swipeAnimationSpec) },
                    launch { middleScale.animateTo(1f, swipeAnimationSpec) },
                    launch { middleOffsetDp.animateTo(32f, swipeAnimationSpec) },
                    launch { middleBlur.animateTo(0f, swipeAnimationSpec) },
                    launch { bottomScale.animateTo(0.9f, swipeAnimationSpec) },
                    launch { bottomOffsetDp.animateTo(16f, swipeAnimationSpec) },
                    launch { newScale.animateTo(0.8f, swipeAnimationSpec) },
                    launch { newOffsetDp.animateTo(0f, swipeAnimationSpec) },
                    launch { newAlpha.animateTo(1f, swipeAnimationSpec) },
                ).joinAll()
                onAction(
                    SwipeUdf.Action.FinishSwiping(movieCardState = movieCardState)
                )
            }
        }

        LaunchedEffect(draggableState.settledValue) {
            val settledValue = draggableState.settledValue
            if (settledValue is MovieCardState.Swiped) {
                startAnimations(movieCardState = settledValue)
            }
        }

        top?.let {
            MovieCard(
                modifier = Modifier
                    .graphicsLayer {
                        translationY = 32 * density
                        translationX = draggableState.offset
                        alpha = topAlpha.value
                    }.clickableWithoutIndication(
                        onClick = { onMovieClick(top.id) }
                    ),
                movie = top,
            )
        }
    }
}

@Composable
private fun MovieCard(
    movie: Movie,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .clip(cardShape)
            .background(color = MovieTheme.colors.surface.main)
            .border(
                width = 0.5.dp,
                color = MovieTheme.colors.stroke,
                shape = cardShape
            )
    ) {
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
                style = MovieTheme.typography.title16,
                color = MovieTheme.colors.text.main,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
            )
            MovieInfo(
                releaseDate = movie.releaseDate,
                voteAverage = movie.voteAverage,
                voteCount = movie.voteCount,
            )
            MovieGenres(movie.genres)
        }
    }
}

@Preview
@Composable
private fun SwipeScreenDataPreview() {
    MovieTheme {
        SwipeScreenContent(
            state = SwipeUdf.State(
                code = "AAAA",
                inviteBannerVisible = true,
                movies = List(3) { i ->
                    Movie.mock
                }
            ),
            onAction = {},
            onMovieClick = {},
        )
    }
}

@Preview
@Composable
private fun SwipeScreenLoadingPreview() {
    MovieTheme {
        SwipeScreenContent(
            state = SwipeUdf.State(
                code = null,
                inviteBannerVisible = false,
                movies = null
            ),
            onAction = {},
            onMovieClick = {},
        )
    }
}