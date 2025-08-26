package com.github.freshmorsikov.moviematcher.feature.join

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
import com.github.freshmorsikov.moviematcher.core.ui.MovieButton
import com.github.freshmorsikov.moviematcher.core.ui.MovieScaffold
import com.github.freshmorsikov.moviematcher.feature.matches.domain.PAIR_ID_ABC
import moviematcher.composeapp.generated.resources.Res
import moviematcher.composeapp.generated.resources.matches_enter_code
import moviematcher.composeapp.generated.resources.matches_save
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun JoinPairScreen() {
    JoinPairContent()
}

@Composable
fun JoinPairContent() {
    MovieScaffold {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
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
private fun JoinPairContentPreview() {
    MaterialTheme {
        JoinPairContent()
    }
}