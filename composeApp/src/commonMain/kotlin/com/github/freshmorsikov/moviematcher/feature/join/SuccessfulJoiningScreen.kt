package com.github.freshmorsikov.moviematcher.feature.join

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.github.freshmorsikov.moviematcher.app.NavigationRoute
import com.github.freshmorsikov.moviematcher.core.ui.MovieButton
import com.github.freshmorsikov.moviematcher.core.ui.MovieScaffold
import moviematcher.composeapp.generated.resources.Res
import moviematcher.composeapp.generated.resources.ic_check
import moviematcher.composeapp.generated.resources.join_find_matches
import moviematcher.composeapp.generated.resources.join_successfully_paired
import moviematcher.composeapp.generated.resources.join_you_can_start_finding
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun SuccessfulJoiningScreen(navController: NavController) {
    MovieScaffold {
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
                navController.navigate(NavigationRoute.Swipe) {
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
private fun SuccessfulJoiningScreenPreview() {
    MaterialTheme {
        SuccessfulJoiningScreen(navController = rememberNavController())
    }
}