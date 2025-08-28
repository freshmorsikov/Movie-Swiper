package com.github.freshmorsikov.moviematcher.feature.matches

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.navigation.NavController
import com.github.freshmorsikov.moviematcher.app.NavigationRoute
import com.github.freshmorsikov.moviematcher.core.ui.MovieButton
import com.github.freshmorsikov.moviematcher.core.ui.MovieScaffold
import com.github.freshmorsikov.moviematcher.core.ui.OutlinedMovieButton
import com.github.freshmorsikov.moviematcher.feature.matches.domain.model.PairState
import com.github.freshmorsikov.moviematcher.feature.matches.presentation.MatchesUdf
import com.github.freshmorsikov.moviematcher.feature.matches.presentation.MatchesViewModel
import com.github.freshmorsikov.moviematcher.util.SubscribeOnEvents
import moviematcher.composeapp.generated.resources.Res
import moviematcher.composeapp.generated.resources.ic_match
import moviematcher.composeapp.generated.resources.matches_create_pair
import moviematcher.composeapp.generated.resources.matches_info
import moviematcher.composeapp.generated.resources.matches_join_pair
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
                        Text(text = state.pairState.code)
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
        Icon(
            modifier = Modifier.size(120.dp),
            painter = painterResource(Res.drawable.ic_match),
            tint = MaterialTheme.colorScheme.secondary,
            contentDescription = null
        )
        Text(
            modifier = Modifier
                .padding(top = 20.dp)
                .fillMaxWidth(),
            text = stringResource(Res.string.matches_info),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground,
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
            text = stringResource(Res.string.matches_join_pair),
            color = MaterialTheme.colorScheme.secondary,
            onClick = {
                onAction(MatchesUdf.Action.JoinPairClick)
            }
        )
    }
}

@Preview
@Composable
private fun MatchesContentPreview() {
    MaterialTheme {
        MatchesContent(
            state = MatchesUdf.State.Data(
                pairState = PairState.NotPaired
            ),
            onAction = {}
        )
    }
}