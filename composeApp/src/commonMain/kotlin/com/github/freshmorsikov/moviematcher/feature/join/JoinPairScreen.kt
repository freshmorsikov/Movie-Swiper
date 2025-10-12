package com.github.freshmorsikov.moviematcher.feature.join

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.github.freshmorsikov.moviematcher.app.NavigationRoute
import com.github.freshmorsikov.moviematcher.core.ui.MovieButton
import com.github.freshmorsikov.moviematcher.core.ui.MovieScaffold
import com.github.freshmorsikov.moviematcher.feature.join.presentation.JoinPairUdf
import com.github.freshmorsikov.moviematcher.feature.join.presentation.JoinPairViewModel
import com.github.freshmorsikov.moviematcher.util.SubscribeOnEvents
import com.github.freshmorsikov.moviematcher.util.clickableWithoutIndication
import moviematcher.composeapp.generated.resources.Res
import moviematcher.composeapp.generated.resources.ic_close
import moviematcher.composeapp.generated.resources.join_enter_code
import moviematcher.composeapp.generated.resources.join_save
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun JoinPairScreen(
    navController: NavController,
    viewModel: JoinPairViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    JoinPairContent(
        state = state,
        onAction = viewModel::onAction
    )
    SubscribeOnEvents(viewModel.event) { event ->
        when (event) {
            JoinPairUdf.Event.GoBack -> {
                navController.popBackStack()
            }
            JoinPairUdf.Event.OpenSuccess -> {
                navController.navigate(NavigationRoute.Pairing) {
                    popUpTo(NavigationRoute.Matches)
                }
            }
        }
    }
}

@Composable
private fun JoinPairContent(
    state: JoinPairUdf.State,
    onAction: (JoinPairUdf.Action) -> Unit
) {
    MovieScaffold {
        Icon(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.TopEnd)
                .size(24.dp)
                .clickableWithoutIndication {
                    onAction(JoinPairUdf.Action.CloseClick)
                },
            painter = painterResource(Res.drawable.ic_close),
            tint = MaterialTheme.colorScheme.secondary,
            contentDescription = null
        )
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.weight(1f))
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(Res.string.join_enter_code),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.secondary,
                textAlign = TextAlign.Center,
            )

            val focusManager = LocalFocusManager.current
            LaunchedEffect(Unit) {
                focusManager.moveFocus(FocusDirection.Next)
            }
            OutlinedTextField(
                modifier = Modifier
                    .width(160.dp)
                    .padding(top = 32.dp),
                value = state.code,
                onValueChange = { value ->
                    onAction(
                        JoinPairUdf.Action.UpdateCode(input = value)
                    )
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
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    cursorColor = Color.Black.copy(alpha = 0.2f),
                    focusedBorderColor = Color.Black.copy(alpha = 0.2f),
                    unfocusedBorderColor = Color.Black.copy(alpha = 0.2f),
                )
            )
            Spacer(modifier = Modifier.weight(1f))
            MovieButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                text = stringResource(Res.string.join_save),
                containerColor = MaterialTheme.colorScheme.secondary,
                enabled = state.saveButtonEnabled,
                onClick = {
                    onAction(JoinPairUdf.Action.SaveCode)
                }
            )
        }
    }
}

@Preview
@Composable
private fun JoinPairContentPreview() {
    MaterialTheme {
        JoinPairContent(
            state = JoinPairUdf.State(code = "AAAA"),
            onAction = {},
        )
    }
}