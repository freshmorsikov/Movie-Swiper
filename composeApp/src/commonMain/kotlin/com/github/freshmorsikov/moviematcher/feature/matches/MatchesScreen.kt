package com.github.freshmorsikov.moviematcher.feature.matches

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import com.github.freshmorsikov.moviematcher.core.ui.MovieButton
import com.github.freshmorsikov.moviematcher.core.ui.MovieScaffold
import com.github.freshmorsikov.moviematcher.feature.matches.presentation.MatchesUdf
import com.github.freshmorsikov.moviematcher.feature.matches.presentation.MatchesViewModel
import moviematcher.composeapp.generated.resources.Res
import moviematcher.composeapp.generated.resources.matches_copy
import moviematcher.composeapp.generated.resources.matches_send_code
import moviematcher.composeapp.generated.resources.matches_share
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MatchesScreen(
    viewModel: MatchesViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    MatchesContent(state = state)
}

@Composable
fun MatchesContent(state: MatchesUdf.State) {
    MovieScaffold {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(Res.string.matches_send_code),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
            )
            state.pairId?.let { pairId ->
                Text(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .background(
                            color = MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp),
                            shape = RoundedCornerShape(8.dp)
                        ).padding(
                            horizontal = 16.dp,
                            vertical = 8.dp,
                        ),
                    text = pairId,
                    style = MaterialTheme.typography.displayMedium.copy(fontWeight = FontWeight.ExtraBold),
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            Row(
                modifier = Modifier.padding(top = 16.dp),
                horizontalArrangement = spacedBy(8.dp),
            ) {
                MovieButton(
                    modifier = Modifier.weight(1f),
                    text = stringResource(Res.string.matches_share),
                    containerColor = MaterialTheme.colorScheme.secondary,
                    onClick = {
                        // TODO handle
                    }
                )
                OutlinedButton(
                    modifier = Modifier.weight(1f),
                    border = BorderStroke(
                        width = 2.dp,
                        color = MaterialTheme.colorScheme.secondary
                    ),
                    onClick = {
                        // TODO handle
                    },
                ) {
                    Text(
                        text = stringResource(Res.string.matches_copy),
                        color = MaterialTheme.colorScheme.secondary,
                    )
                }
            }

        }
    }
}