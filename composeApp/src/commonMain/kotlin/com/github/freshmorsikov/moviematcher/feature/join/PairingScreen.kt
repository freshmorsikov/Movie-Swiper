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
import moviematcher.composeapp.generated.resources.ic_close
import moviematcher.composeapp.generated.resources.pairing_close
import moviematcher.composeapp.generated.resources.pairing_error_text
import moviematcher.composeapp.generated.resources.pairing_error_title
import moviematcher.composeapp.generated.resources.pairing_find_matches
import moviematcher.composeapp.generated.resources.pairing_success_text
import moviematcher.composeapp.generated.resources.pairing_success_title
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
                    color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.6f),
                    shape = RoundedCornerShape(8.dp)
                )
        ) {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(16.dp)
                    .size(24.dp),
                color = MaterialTheme.colorScheme.onSecondary,
                strokeWidth = 2.dp,
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
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.secondary,
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
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.secondary,
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
                .padding(16.dp),
            text = stringResource(resource = buttonTextRes),
            containerColor = MaterialTheme.colorScheme.secondary,
            onClick = {
                navController.navigate(NavigationRoute.Swipe()) {
                    popUpTo(NavigationRoute.Pairing::class) {
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
    val color = if (isSuccess) {
        Color(0xFF00BE64)
    } else {
        Color(0xFFEB5757)
    }
    val iconRes = if (isSuccess) {
        Res.drawable.ic_check
    } else {
        Res.drawable.ic_close
    }
    Icon(
        modifier = modifier
            .size(128.dp)
            .background(
                color = color,
                shape = CircleShape,
            )
            .padding(40.dp),
        painter = painterResource(resource = iconRes),
        tint = MaterialTheme.colorScheme.onPrimary,
        contentDescription = "Success check",
    )
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
            state = PairingUdf.State.Result(isSuccess = true)
        )
    }
}

@Preview
@Composable
private fun ErrorPairingContentPreview() {
    MaterialTheme {
        PairingContent(
            navController = rememberNavController(),
            state = PairingUdf.State.Result(isSuccess = false)
        )
    }
}