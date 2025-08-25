package com.github.freshmorsikov.moviematcher.feature.matches

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.freshmorsikov.moviematcher.core.ui.MovieButton
import com.github.freshmorsikov.moviematcher.core.ui.MovieScaffold
import com.github.freshmorsikov.moviematcher.core.ui.OutlinedMovieButton
import com.github.freshmorsikov.moviematcher.feature.matches.domain.PAIR_ID_ABC
import com.github.freshmorsikov.moviematcher.feature.matches.presentation.MatchesUdf
import com.github.freshmorsikov.moviematcher.feature.matches.presentation.MatchesViewModel
import moviematcher.composeapp.generated.resources.Res
import moviematcher.composeapp.generated.resources.matches_copy
import moviematcher.composeapp.generated.resources.matches_enter_code
import moviematcher.composeapp.generated.resources.matches_save
import moviematcher.composeapp.generated.resources.matches_send_code
import moviematcher.composeapp.generated.resources.matches_share
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
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
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = spacedBy(40.dp),
        ) {
            SendCodeBlock(pairId = state.pairId)
            Divider()
            EnterCodeBlock()
        }
    }
}

@Composable
private fun SendCodeBlock(
    pairId: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(Res.string.matches_send_code),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
            )
            Text(
                modifier = Modifier
                    .padding(top = 16.dp)
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
                OutlinedMovieButton(
                    modifier = Modifier.weight(1f),
                    text = stringResource(Res.string.matches_copy),
                    color = MaterialTheme.colorScheme.secondary,
                    onClick = {
                        // TODO handle
                    }
                )
            }
        }
    }
}

@Composable
private fun Divider(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        horizontalArrangement = spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        HorizontalDivider(
            modifier = Modifier.weight(1f),
            color = MaterialTheme.colorScheme.outlineVariant,
        )
        Text(
            text = "OR",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        HorizontalDivider(
            modifier = Modifier.weight(1f),
            color = MaterialTheme.colorScheme.outlineVariant,
        )
    }
}

@Composable
private fun EnterCodeBlock(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(Res.string.matches_enter_code),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
            )

            val focusManager = LocalFocusManager.current
            var input by remember { mutableStateOf("") }
            OutlinedTextField(
                modifier = Modifier
                    .width(160.dp)
                    .padding(top = 16.dp),
                value = input,
                onValueChange = { value ->
                    input = value.filter { char ->
                        PAIR_ID_ABC.contains(
                            char = char,
                            ignoreCase = true,
                        )
                    }.map { char ->
                        if (char.isLetter()) {
                            char.uppercase()
                        } else {
                            char
                        }
                    }.take(4).joinToString("")
                },
                textStyle = MaterialTheme.typography.displayMedium
                    .copy(
                        fontWeight = FontWeight.ExtraBold,
                        textAlign = TextAlign.Center,
                    ),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                    }
                ),
                singleLine = true,
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = MaterialTheme.colorScheme.onBackground,
                    unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp),
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp),
                    cursorColor = MaterialTheme.colorScheme.outlineVariant,
                    focusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                )
            )
            MovieButton(
                modifier = Modifier
                    .fillMaxWidth(fraction = 0.5f)
                    .padding(top = 16.dp),
                text = stringResource(Res.string.matches_save),
                containerColor = MaterialTheme.colorScheme.secondary,
                onClick = {
                    // TODO handle
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
            state = MatchesUdf.State(
                pairId = "AB17"
            )
        )
    }
}