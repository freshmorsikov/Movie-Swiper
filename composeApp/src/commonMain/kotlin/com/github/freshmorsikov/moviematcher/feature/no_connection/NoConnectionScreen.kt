package com.github.freshmorsikov.moviematcher.feature.no_connection

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.freshmorsikov.moviematcher.core.ui.MovieScaffold
import com.github.freshmorsikov.moviematcher.core.ui.PrimaryMovieButton
import com.github.freshmorsikov.moviematcher.core.ui.theme.MovieTheme
import moviematcher.composeapp.generated.resources.Res
import moviematcher.composeapp.generated.resources.no_connection_retry
import moviematcher.composeapp.generated.resources.no_connection_subtitle
import moviematcher.composeapp.generated.resources.no_connection_title
import moviematcher.composeapp.generated.resources.popcorny_no_connection
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun NoConnectionScreen() {
    MovieScaffold {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                modifier = Modifier.width(width = 450.dp),
                painter = painterResource(Res.drawable.popcorny_no_connection),
                contentDescription = null,
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
                text = stringResource(Res.string.no_connection_title),
                style = MovieTheme.typography.title20,
                color = MovieTheme.colors.text.main,
                textAlign = TextAlign.Center,
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                text = stringResource(Res.string.no_connection_subtitle),
                style = MovieTheme.typography.body16,
                color = MovieTheme.colors.text.variant,
                textAlign = TextAlign.Center,
            )
        }
        PrimaryMovieButton(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(16.dp)
                .padding(
                    bottom = WindowInsets.systemBars.asPaddingValues().calculateBottomPadding()
                ),
            text = stringResource(Res.string.no_connection_retry),
            onClick = {},
        )
    }
}

@Preview
@Composable
fun NoConnectionScreenPreview() {
    MovieTheme {
        NoConnectionScreen()
    }
}