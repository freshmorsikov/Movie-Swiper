package com.github.freshmorsikov.moviematcher.feature.swipe

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import com.github.freshmorsikov.moviematcher.util.toRatingFormat
import moviematcher.composeapp.generated.resources.Res
import moviematcher.composeapp.generated.resources.ic_star
import org.jetbrains.compose.resources.painterResource
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
        state.movieList.forEach { movie ->
            MovieCard(
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.Center),
                movie = movie,
                swipingMovieId = state.swipingMovieId
            )
        }

        Row(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.BottomCenter),
            horizontalArrangement = spacedBy(16.dp)
        ) {
            MovieButton(
                modifier = Modifier.weight(1f),
                text = "Dislike",
                containerColor = Color(0xFFF95667),
                enabled = state.swipingMovieId == null,
                onClick = {
                    onAction(SwipeUdf.Action.Dislike)
                }
            )
            MovieButton(
                modifier = Modifier.weight(1f),
                text = "Like",
                containerColor = Color(0xFF00BE64),
                enabled = state.swipingMovieId == null,
                onClick = {
                    onAction(SwipeUdf.Action.Like)
                }
            )
        }
    }
}

@Composable
private fun MovieCard(
    movie: Movie,
    swipingMovieId: SwipeUdf.SwipingMovieId?,
    modifier: Modifier = Modifier,
) {
    AnimatedVisibility(
        modifier = modifier,
        visible = movie.id != swipingMovieId?.id,
        enter = EnterTransition.None,
        exit = fadeOut(tween(durationMillis = 500)) + slideOutHorizontally { fullWidth ->
            if (swipingMovieId is SwipeUdf.SwipingMovieId.Left) {
                -fullWidth
            } else {
                fullWidth
            }
        },
    ) {
        Card(
            modifier = modifier,
            colors = CardDefaults.cardColors(
                containerColor = Color.White
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

                    Row(horizontalArrangement = spacedBy(8.dp)) {
                        Text(
                            text = movie.releaseDate.take(4),
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray,
                        )
                        Text(
                            text = "|",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray,
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = spacedBy(2.dp)
                        ) {
                            Icon(
                                modifier = Modifier.size(14.dp),
                                painter = painterResource(Res.drawable.ic_star),
                                tint = Color(0xFFEAAF00),
                                contentDescription = null
                            )
                            Text(
                                text = movie.voteAverage.toRatingFormat(),
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color(0xFFEAAF00),
                            )
                        }
                    }

                    FlowRow(
                        horizontalArrangement = spacedBy(8.dp),
                        verticalArrangement = spacedBy(8.dp),
                    ) {
                        movie.genres.forEach { genre ->
                            Text(
                                modifier = Modifier
                                    .background(Color.LightGray, RoundedCornerShape(4.dp))
                                    .padding(horizontal = 6.dp, vertical = 2.dp),
                                text = genre,
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.DarkGray,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun SwipeScreenContentPreview() {
    SwipeScreenContent(
        state = SwipeUdf.State(
            movieList = emptyList(),
            swipingMovieId = null
        ),
        onAction = {}
    )
}