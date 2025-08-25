package com.github.freshmorsikov.moviematcher.feature.pair

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.github.freshmorsikov.moviematcher.core.ui.MovieButton
import com.github.freshmorsikov.moviematcher.core.ui.MovieScaffold
import com.github.freshmorsikov.moviematcher.core.ui.OutlinedMovieButton
import moviematcher.composeapp.generated.resources.Res
import moviematcher.composeapp.generated.resources.ic_close
import moviematcher.composeapp.generated.resources.pair_copy
import moviematcher.composeapp.generated.resources.pair_send_code
import moviematcher.composeapp.generated.resources.pair_share
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun PairScreen() {
    PairContent("AAAA")
}

@Composable
fun PairContent(pairId: String) {
    MovieScaffold {
        Icon(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.TopEnd)
                .size(24.dp)
                .clickable {
                    // TODO go back
                },
            painter = painterResource(Res.drawable.ic_close),
            tint = MaterialTheme.colorScheme.secondary,
            contentDescription = null
        )
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = spacedBy(32.dp)
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(Res.string.pair_send_code),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
            )
            Text(
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp),
                        shape = RoundedCornerShape(8.dp)
                    ).border(
                        width = 0.5.dp,
                        color = MaterialTheme.colorScheme.outlineVariant,
                        shape = RoundedCornerShape(8.dp),
                    ).padding(
                        horizontal = 16.dp,
                        vertical = 8.dp,
                    ),
                text = pairId,
                style = MaterialTheme.typography.displayMedium.copy(fontWeight = FontWeight.ExtraBold),
                color = MaterialTheme.colorScheme.onBackground
            )
            Row(horizontalArrangement = spacedBy(8.dp)) {
                MovieButton(
                    modifier = Modifier.weight(1f),
                    text = stringResource(Res.string.pair_share),
                    containerColor = MaterialTheme.colorScheme.secondary,
                    onClick = {
                        // TODO handle
                    }
                )
                OutlinedMovieButton(
                    modifier = Modifier.weight(1f),
                    text = stringResource(Res.string.pair_copy),
                    color = MaterialTheme.colorScheme.secondary,
                    onClick = {
                        // TODO handle
                    }
                )
            }
        }
    }
}

@Preview
@Composable
private fun PairContentPreview() {
    MaterialTheme {
        PairContent(pairId = "AB17")
    }
}