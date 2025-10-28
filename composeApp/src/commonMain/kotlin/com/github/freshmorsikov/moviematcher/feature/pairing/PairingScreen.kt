package com.github.freshmorsikov.moviematcher.feature.pairing

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.github.freshmorsikov.moviematcher.app.navigation.NavigationRoute
import com.github.freshmorsikov.moviematcher.core.ui.MovieButton
import com.github.freshmorsikov.moviematcher.core.ui.MovieScaffold
import com.github.freshmorsikov.moviematcher.core.ui.theme.MovieTheme
import com.github.freshmorsikov.moviematcher.feature.pairing.presentation.PairingUdf
import com.github.freshmorsikov.moviematcher.feature.pairing.presentation.PairingViewModel
import moviematcher.composeapp.generated.resources.Res
import moviematcher.composeapp.generated.resources.pairing_close
import moviematcher.composeapp.generated.resources.pairing_error_text
import moviematcher.composeapp.generated.resources.pairing_error_title
import moviematcher.composeapp.generated.resources.pairing_find_matches
import moviematcher.composeapp.generated.resources.pairing_success_text
import moviematcher.composeapp.generated.resources.pairing_success_title
import moviematcher.composeapp.generated.resources.popcorny_failed
import moviematcher.composeapp.generated.resources.popcorny_success
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PairingScreen(
    navController: NavController,
    code: String?,
    viewModel: PairingViewModel = koinViewModel(),
) {
    LaunchedEffect(code) {
        viewModel.onAction(
            PairingUdf.Action.HandleCode(code = code)
        )
    }

    val state by viewModel.state.collectAsStateWithLifecycle()
    PairingContent(
        navController = navController,
        state = state,
    )
}

@Composable
private fun PairingContent(
    navController: NavController,
    state: PairingUdf.State,
) {
    MovieScaffold {
        AnimatedContent(targetState = state) { targetState ->
            when (targetState) {
                PairingUdf.State.Loading -> {
                    LoadingContent()
                }

                is PairingUdf.State.Result -> {
                    ResultContent(
                        navController = navController,
                        isSuccess = targetState.isSuccess
                    )
                }
            }
        }
    }
}


@Composable
private fun LoadingContent() {
    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .background(
                    color = MovieTheme.colors.primary.copy(alpha = 0.6f),
                    shape = RoundedCornerShape(8.dp)
                )
        ) {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(16.dp)
                    .size(24.dp),
                color = MovieTheme.colors.text.onAccent,
                strokeWidth = 4.dp,
            )
        }
    }
}

@Composable
private fun ResultContent(
    navController: NavController,
    isSuccess: Boolean,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            PairingIcon(isSuccess = isSuccess)
            val titleRes = if (isSuccess) {
                Res.string.pairing_success_title
            } else {
                Res.string.pairing_error_title
            }
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
                text = stringResource(resource = titleRes),
                style = MovieTheme.typography.title16,
                color = MovieTheme.colors.text.variant,
                textAlign = TextAlign.Center,
            )
            val textRes = if (isSuccess) {
                Res.string.pairing_success_text
            } else {
                Res.string.pairing_error_text
            }
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                text = stringResource(resource = textRes),
                style = MovieTheme.typography.body14,
                color = MovieTheme.colors.text.variant,
                textAlign = TextAlign.Center,
            )
        }
        val buttonTextRes = if (isSuccess) {
            Res.string.pairing_find_matches
        } else {
            Res.string.pairing_close
        }
        MovieButton(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(16.dp)
                .padding(
                    bottom = WindowInsets.systemBars.asPaddingValues().calculateBottomPadding()
                ),
            text = stringResource(resource = buttonTextRes),
            containerColor = MovieTheme.colors.primary,
            contentColor = MovieTheme.colors.text.onAccent,
            onClick = {
                navController.navigate(NavigationRoute.Swipe) {
                    popUpTo(navController.graph.id) {
                        inclusive = true
                    }
                }
            }
        )
    }
}

@Composable
private fun PairingIcon(
    isSuccess: Boolean,
    modifier: Modifier = Modifier,
) {
    val resource = if (isSuccess) {
        Res.drawable.popcorny_success
    } else {
        Res.drawable.popcorny_failed
    }
    Image(
        modifier = modifier.width(width = 360.dp),
        painter = painterResource(resource),
        contentDescription = null,
    )
}

@Preview
@Composable
private fun LoadingPairingContentPreview() {
    MovieTheme {
        PairingContent(
            navController = rememberNavController(),
            state = PairingUdf.State.Loading
        )
    }
}

@Preview
@Composable
private fun SuccessPairingContentPreview() {
    MovieTheme {
        PairingContent(
            navController = rememberNavController(),
            state = PairingUdf.State.Result(isSuccess = true)
        )
    }
}

@Preview
@Composable
private fun ErrorPairingContentPreview() {
    MovieTheme {
        PairingContent(
            navController = rememberNavController(),
            state = PairingUdf.State.Result(isSuccess = false)
        )
    }
}