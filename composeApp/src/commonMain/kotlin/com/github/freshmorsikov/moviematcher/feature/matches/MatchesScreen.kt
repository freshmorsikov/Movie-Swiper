package com.github.freshmorsikov.moviematcher.feature.matches

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.github.freshmorsikov.moviematcher.app.NavigationRoute
import com.github.freshmorsikov.moviematcher.core.ui.MovieButton
import com.github.freshmorsikov.moviematcher.core.ui.MovieScaffold
import com.github.freshmorsikov.moviematcher.core.ui.OutlinedMovieButton
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
    MatchesContent(onAction = viewModel::onAction)
    SubscribeOnEvents(viewModel.event) { event ->
        when (event) {
            is MatchesUdf.Event.OpenPair -> {
                navController.navigate(route = NavigationRoute.Pair)
            }

            is MatchesUdf.Event.OpenJoinPair -> {
                navController.navigate(route = NavigationRoute.JoinPair)
            }
        }
    }
}

@Composable
fun MatchesContent(onAction: (MatchesUdf.Action) -> Unit) {
    MovieScaffold {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
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
}

@Preview
@Composable
private fun MatchesContentPreview() {
    MaterialTheme {
        MatchesContent(
            onAction = {}
        )
    }
}