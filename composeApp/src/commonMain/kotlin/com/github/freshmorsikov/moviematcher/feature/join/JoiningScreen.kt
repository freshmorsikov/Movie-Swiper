package com.github.freshmorsikov.moviematcher.feature.join

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.github.freshmorsikov.moviematcher.app.NavigationRoute
import com.github.freshmorsikov.moviematcher.core.ui.MovieButton
import com.github.freshmorsikov.moviematcher.core.ui.MovieScaffold
import com.github.freshmorsikov.moviematcher.feature.join.presentation.PairingUdf
import com.github.freshmorsikov.moviematcher.feature.join.presentation.PairingViewModel
import moviematcher.composeapp.generated.resources.Res
import moviematcher.composeapp.generated.resources.ic_check
import moviematcher.composeapp.generated.resources.join_find_matches
import moviematcher.composeapp.generated.resources.join_successfully_paired
import moviematcher.composeapp.generated.resources.join_you_can_start_finding
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

                PairingUdf.State.Success -> {
                    SuccessContent(navController = navController)
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
                    color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.6f),
                    shape = RoundedCornerShape(8.dp)
                )
        ) {
            CircularProgressIndicator(
                modifier = Modifier
                    .padding(16.dp)
                    .size(40.dp),
                color = MaterialTheme.colorScheme.onSecondary,
                strokeWidth = 4.dp,
            )
        }
    }
}

@Composable
private fun SuccessContent(navController: NavController) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(
                modifier = Modifier
                    .background(
                        color = Color(0xFF00BE64),
                        shape = CircleShape,
                    )
                    .padding(28.dp)
                    .size(64.dp),
                painter = painterResource(Res.drawable.ic_check),
                tint = MaterialTheme.colorScheme.onPrimary,
                contentDescription = "Success check",
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
                text = stringResource(Res.string.join_successfully_paired),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.secondary,
                textAlign = TextAlign.Center,
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                text = stringResource(Res.string.join_you_can_start_finding),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.secondary,
                textAlign = TextAlign.Center,
            )
        }
        MovieButton(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(16.dp),
            text = stringResource(Res.string.join_find_matches),
            containerColor = MaterialTheme.colorScheme.secondary,
            onClick = {
                navController.navigate(NavigationRoute.Swipe()) {
                    popUpTo(NavigationRoute.Matches) {
                        inclusive = true
                    }
                }
            }
        )
    }
}

@Preview
@Composable
private fun LoadingPairingContentPreview() {
    MaterialTheme {
        PairingContent(
            navController = rememberNavController(),
            state = PairingUdf.State.Loading
        )
    }
}

@Preview
@Composable
private fun SuccessPairingContentPreview() {
    MaterialTheme {
        PairingContent(
            navController = rememberNavController(),
            state = PairingUdf.State.Success
        )
    }
}