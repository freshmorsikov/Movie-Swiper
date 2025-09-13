package com.github.freshmorsikov.moviematcher.feature.code

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.github.freshmorsikov.moviematcher.core.ui.MovieButton
import com.github.freshmorsikov.moviematcher.core.ui.MovieScaffold
import com.github.freshmorsikov.moviematcher.core.ui.OutlinedMovieButton
import com.github.freshmorsikov.moviematcher.feature.code.presentation.CodeUdf
import com.github.freshmorsikov.moviematcher.feature.code.presentation.CodeViewModel
import com.github.freshmorsikov.moviematcher.util.SharingManager
import com.github.freshmorsikov.moviematcher.util.SubscribeOnEvents
import com.github.freshmorsikov.moviematcher.util.clickableWithoutIndication
import com.github.freshmorsikov.moviematcher.util.clipEntryOf
import moviematcher.composeapp.generated.resources.Res
import moviematcher.composeapp.generated.resources.code_copy
import moviematcher.composeapp.generated.resources.code_send
import moviematcher.composeapp.generated.resources.code_share
import moviematcher.composeapp.generated.resources.ic_close
import moviematcher.composeapp.generated.resources.sharing_title
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CodeScreen(
    navController: NavController,
    viewModel: CodeViewModel = koinViewModel(),
    sharingManager: SharingManager = koinInject(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    CodeContent(
        code = state.code,
        onAction = viewModel::onAction
    )

    val clipboard = LocalClipboard.current
    val sharingTitle = stringResource(Res.string.sharing_title)
    SubscribeOnEvents(viewModel.event) { event ->
        when (event) {
            CodeUdf.Event.GoBack -> {
                navController.popBackStack()
            }

            is CodeUdf.Event.SaveToClipboard -> {
                clipboard.setClipEntry(
                    clipEntry = clipEntryOf(
                        string = event.code
                    )
                )
            }

            is CodeUdf.Event.ShowSharingDialog -> {
                sharingManager.share(
                    title = sharingTitle,
                    text = event.code,
                )
            }
        }
    }
}

@Composable
fun CodeContent(
    code: String,
    onAction: (CodeUdf.Action) -> Unit
) {
    MovieScaffold {
        Icon(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.TopEnd)
                .size(24.dp)
                .clickableWithoutIndication {
                    onAction(CodeUdf.Action.CloseClick)
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
                text = stringResource(Res.string.code_send),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.secondary,
                textAlign = TextAlign.Center,
            )
            Text(
                modifier = Modifier
                    .padding(top = 24.dp)
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(8.dp)
                    ).border(
                        width = 0.5.dp,
                        color = Color.Black.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(8.dp),
                    ).padding(
                        horizontal = 16.dp,
                        vertical = 8.dp,
                    ),
                text = code,
                style = MaterialTheme.typography.displayMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                ),
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.weight(1f))

            OutlinedMovieButton(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(Res.string.code_copy),
                color = MaterialTheme.colorScheme.secondary,
                onClick = {
                    onAction(CodeUdf.Action.CopyClick)
                }
            )
            MovieButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                text = stringResource(Res.string.code_share),
                containerColor = MaterialTheme.colorScheme.secondary,
                onClick = {
                    onAction(CodeUdf.Action.ShareClick)
                }
            )
        }
    }
}

@Preview
@Composable
private fun CodeContentPreview() {
    MaterialTheme {
        CodeContent(
            code = "AB17",
            onAction = {}
        )
    }
}