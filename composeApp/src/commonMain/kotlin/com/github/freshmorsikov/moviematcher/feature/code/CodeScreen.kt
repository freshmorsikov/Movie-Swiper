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
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import moviematcher.composeapp.generated.resources.ic_close
import moviematcher.composeapp.generated.resources.code_copy
import moviematcher.composeapp.generated.resources.code_send
import moviematcher.composeapp.generated.resources.code_share
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
        pairId = state.pairId,
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
                        string = event.pairId
                    )
                )
            }

            is CodeUdf.Event.ShowSharingDialog -> {
                sharingManager.share(
                    title = sharingTitle,
                    text = event.pairId,
                )
            }
        }
    }
}

@Composable
fun CodeContent(
    pairId: String,
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
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
            )
            Text(
                modifier = Modifier
                    .padding(top = 32.dp)
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
                style = MaterialTheme.typography.displayMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                ),
                color = MaterialTheme.colorScheme.onBackground
            )
            OutlinedMovieButton(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth(fraction = 0.5f),
                text = stringResource(Res.string.code_copy),
                color = MaterialTheme.colorScheme.secondary,
                onClick = {
                    onAction(CodeUdf.Action.CopyClick)
                }
            )
            Spacer(modifier = Modifier.weight(1f))
            MovieButton(
                modifier = Modifier.fillMaxWidth(),
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
            pairId = "AB17",
            onAction = {}
        )
    }
}