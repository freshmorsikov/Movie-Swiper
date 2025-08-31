package com.github.freshmorsikov.moviematcher.feature.matches

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.github.freshmorsikov.moviematcher.app.NavigationRoute
import com.github.freshmorsikov.moviematcher.core.ui.MovieButton
import com.github.freshmorsikov.moviematcher.core.ui.MovieScaffold
import com.github.freshmorsikov.moviematcher.core.ui.OutlinedMovieButton
import com.github.freshmorsikov.moviematcher.feature.matches.domain.model.PairState
import com.github.freshmorsikov.moviematcher.feature.matches.presentation.MatchesUdf
import com.github.freshmorsikov.moviematcher.feature.matches.presentation.MatchesViewModel
import com.github.freshmorsikov.moviematcher.feature.swipe.domain.model.Movie
import com.github.freshmorsikov.moviematcher.util.SubscribeOnEvents
import moviematcher.composeapp.generated.resources.Res
import moviematcher.composeapp.generated.resources.ic_match
import moviematcher.composeapp.generated.resources.matches_create_or_join
import moviematcher.composeapp.generated.resources.matches_create_pair
import moviematcher.composeapp.generated.resources.matches_empty
import moviematcher.composeapp.generated.resources.matches_join_with_code
import moviematcher.composeapp.generated.resources.matches_pair_up
import moviematcher.composeapp.generated.resources.matches_paired_with
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MatchesScreen(
    navController: NavController,
    viewModel: MatchesViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    MatchesContent(
        state = state,
        onAction = viewModel::onAction
    )
    SubscribeOnEvents(viewModel.event) { event ->
        when (event) {
            is MatchesUdf.Event.OpenCode -> {
                navController.navigate(route = NavigationRoute.Code)
            }

            is MatchesUdf.Event.OpenJoinPair -> {
                navController.navigate(route = NavigationRoute.JoinPair)
            }
        }
    }
}

@Composable
fun MatchesContent(
    state: MatchesUdf.State,
    onAction: (MatchesUdf.Action) -> Unit
) {
    MovieScaffold {
        when (state) {
            MatchesUdf.State.Loading -> {}
            is MatchesUdf.State.Data -> {
                when (state.pairState) {
                    PairState.NotPaired -> {
                        CreatePairContent(onAction = onAction)
                    }

                    is PairState.Paired -> {
                        PairedContent(
                            code = state.pairState.code,
                            onAction = onAction
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CreatePairContent(
    onAction: (MatchesUdf.Action) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.weight(1f))
        MatchesInfo(text = stringResource(Res.string.matches_pair_up))
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            text = stringResource(Res.string.matches_create_or_join),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.secondary,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.weight(1f))

        MovieButton(
            modifier = Modifier
                .padding(top = 32.dp)
                .fillMaxWidth(),
            text = stringResource(Res.string.matches_create_pair),
            containerColor = MaterialTheme.colorScheme.secondary,
            onClick = {
                onAction(MatchesUdf.Action.CreatePairClick)
            }
        )
        OutlinedMovieButton(
            modifier = Modifier
                .padding(top = 4.dp)
                .fillMaxWidth(),
            text = stringResource(Res.string.matches_join_with_code),
            color = MaterialTheme.colorScheme.secondary,
            onClick = {
                onAction(MatchesUdf.Action.JoinPairClick)
            }
        )
    }
}

@Composable
private fun PairedContent(
    code: String,
    onAction: (MatchesUdf.Action) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = stringResource(Res.string.matches_paired_with),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
            )
            Text(
                modifier = Modifier
                    .padding(start = 4.dp)
                    .background(
                        color = MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp),
                        shape = RoundedCornerShape(4.dp)
                    ).border(
                        width = 0.5.dp,
                        color = MaterialTheme.colorScheme.outlineVariant,
                        shape = RoundedCornerShape(4.dp),
                    ).padding(
                        horizontal = 8.dp,
                        vertical = 4.dp,
                    ),
                text = code,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold,
                ),
                color = MaterialTheme.colorScheme.onBackground,
            )
        }

        Spacer(modifier = Modifier.weight(1f))
        MatchesInfo(text = stringResource(Res.string.matches_empty))
        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
private fun MatchesInfo(text: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            modifier = Modifier.size(120.dp),
            painter = painterResource(Res.drawable.ic_match),
            tint = MaterialTheme.colorScheme.secondary,
            contentDescription = null
        )
        Text(
            modifier = Modifier
                .padding(top = 24.dp)
                .fillMaxWidth(),
            text = text,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.secondary,
            textAlign = TextAlign.Center,
        )
    }
}

@Preview
@Composable
private fun NotPairedPreview() {
    MaterialTheme {
        MatchesContent(
            state = MatchesUdf.State.Data(
                pairState = PairState.NotPaired
            ),
            onAction = {}
        )
    }
}

@Preview
@Composable
private fun PairedPreview() {
    MaterialTheme {
        MatchesContent(
            state = MatchesUdf.State.Data(
                pairState = PairState.Paired(
                    code = "XXXX",
                    matchedMovieList = List(6) { i -> Movie.mock }
                )
            ),
            onAction = {}
        )
    }
}