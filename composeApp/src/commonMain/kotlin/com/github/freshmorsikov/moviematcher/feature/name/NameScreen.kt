package com.github.freshmorsikov.moviematcher.feature.name

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.github.freshmorsikov.moviematcher.core.ui.MovieScaffold
import com.github.freshmorsikov.moviematcher.core.ui.MovieTextField
import com.github.freshmorsikov.moviematcher.core.ui.PrimaryMovieButton
import com.github.freshmorsikov.moviematcher.core.ui.theme.MovieTheme
import com.github.freshmorsikov.moviematcher.feature.name.presentation.NameUdf
import com.github.freshmorsikov.moviematcher.feature.name.presentation.NameViewModel
import com.github.freshmorsikov.moviematcher.util.SubscribeOnEvents
import moviematcher.composeapp.generated.resources.Res
import moviematcher.composeapp.generated.resources.name
import moviematcher.composeapp.generated.resources.name_hi
import moviematcher.composeapp.generated.resources.name_hi_i_am
import moviematcher.composeapp.generated.resources.name_what_is_your_name
import moviematcher.composeapp.generated.resources.name_your_name
import moviematcher.composeapp.generated.resources.popcorny_hello
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun NameScreen(
    navController: NavController,
    pairingCode: String?,
    viewModel: NameViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    SubscribeOnEvents(viewModel.event) { event ->
        when (event) {
            NameUdf.Event.NavigateToSwipe -> {
                navController.navigate(route = NavigationRoute.Swipe) {
                    popUpTo(NavigationRoute.Name::class) {
                        inclusive = true
                    }
                }
            }

            is NameUdf.Event.NavigateToPairing -> {
                navController.navigate(route = NavigationRoute.Pairing(code = event.pairingCode)) {
                    popUpTo(NavigationRoute.Name::class) {
                        inclusive = true
                    }
                }
            }
        }
    }

    NameScreenContent(
        state = state,
        onAction = viewModel::onAction,
        onClick = {
            viewModel.onAction(
                action = NameUdf.Action.Submit(
                    pairingCode = pairingCode
                )
            )
        }
    )
}

@Composable
private fun NameScreenContent(
    state: NameUdf.State,
    onAction: (NameUdf.Action) -> Unit,
    onClick: () -> Unit
) {
    MovieScaffold {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.weight(1f))
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Image(
                    modifier = Modifier.width(width = 360.dp),
                    painter = painterResource(Res.drawable.popcorny_hello),
                    contentDescription = null,
                )
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    text = stringResource(
                        Res.string.name_hi_i_am,
                        stringResource(Res.string.name)
                    ),
                    style = MovieTheme.typography.title20,
                    color = MovieTheme.colors.text.main,
                    textAlign = TextAlign.Center
                )
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    text = stringResource(Res.string.name_what_is_your_name),
                    style = MovieTheme.typography.title16,
                    color = MovieTheme.colors.text.variant,
                    textAlign = TextAlign.Center
                )
            }

            MovieTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp),
                value = state.name,
                onValueChange = { name ->
                    onAction(NameUdf.Action.UpdateName(value = name))
                },
                placeholder = stringResource(Res.string.name_your_name),
            )
            Spacer(modifier = Modifier.weight(1f))

            PrimaryMovieButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        bottom = WindowInsets.systemBars.asPaddingValues().calculateBottomPadding()
                    ),
                text = stringResource(resource = Res.string.name_hi),
                onClick = onClick,
                enabled = state.name.isNotBlank(),
                isLoading = state.isLoading,
            )
        }

    }
}

@Preview
@Composable
private fun NameScreenPreview() {
    MovieTheme {
        NameScreenContent(
            state = NameUdf.State(
                name = "",
                isLoading = false,
            ),
            onAction = {},
            onClick = {},
        )
    }
}
