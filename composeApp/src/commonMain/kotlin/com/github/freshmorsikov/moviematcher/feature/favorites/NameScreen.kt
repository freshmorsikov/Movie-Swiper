package com.github.freshmorsikov.moviematcher.feature.favorites

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.github.freshmorsikov.moviematcher.app.navigation.NavigationRoute
import com.github.freshmorsikov.moviematcher.core.ui.MovieButton
import com.github.freshmorsikov.moviematcher.core.ui.MovieScaffold
import com.github.freshmorsikov.moviematcher.core.ui.MovieTextField
import com.github.freshmorsikov.moviematcher.core.ui.theme.MovieTheme
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

@Composable
fun NameScreen(navController: NavController) {
    FavoriteScreenContent(
        onClick = {
            navController.navigate(route = NavigationRoute.Swipe) {
                popUpTo(NavigationRoute.Name) {
                    inclusive = true
                }
            }
        }
    )
}

@Composable
private fun FavoriteScreenContent(
    onClick: () -> Unit
) {
    MovieScaffold {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(Modifier.weight(1f))
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

            var name by remember {
                mutableStateOf("")
            }
            MovieTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp),
                value = name,
                onValueChange = {
                    name = it
                },
                placeholder = stringResource(Res.string.name_your_name),
            )
            Spacer(Modifier.weight(1f))

            MovieButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        bottom = WindowInsets.systemBars.asPaddingValues().calculateBottomPadding()
                    ),
                text = stringResource(resource = Res.string.name_hi),
                containerColor = MovieTheme.colors.primary,
                contentColor = MovieTheme.colors.text.onAccent,
                onClick = onClick,
                enabled = name.isNotBlank(),
            )
        }

    }
}

@Preview
@Composable
private fun NameScreenPreview() {
    MovieTheme {
        FavoriteScreenContent(onClick = {})
    }
}
