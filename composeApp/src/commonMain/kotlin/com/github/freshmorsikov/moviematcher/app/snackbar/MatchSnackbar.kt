package com.github.freshmorsikov.moviematcher.app.snackbar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarVisuals
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.github.freshmorsikov.moviematcher.app.navigation.NavigationRoute
import com.github.freshmorsikov.moviematcher.core.ui.theme.MovieTheme
import com.github.freshmorsikov.moviematcher.util.clickableWithoutIndication
import moviematcher.composeapp.generated.resources.Res
import moviematcher.composeapp.generated.resources.ic_close
import org.jetbrains.compose.resources.painterResource

class MatchSnackbarVisuals(
    val title: String,
    override val message: String,
    override val withDismissAction: Boolean = false,
    override val duration: SnackbarDuration = SnackbarDuration.Short,
    override val actionLabel: String? = null,
) : SnackbarVisuals

enum class SnackbarSwipeState {
    Visible,
    Swiped
}

@Composable
fun MatchSnackbar(
    visuals: MatchSnackbarVisuals,
    navController: NavController,
    onDismiss: () -> Unit,
) {
    val draggableState = remember {
        AnchoredDraggableState(
            initialValue = SnackbarSwipeState.Visible,
            anchors = DraggableAnchors {
                SnackbarSwipeState.Visible at 0f
                SnackbarSwipeState.Swiped at -100f
            }
        )
    }
    LaunchedEffect(draggableState.currentValue) {
        if (draggableState.currentValue == SnackbarSwipeState.Swiped) {
            onDismiss()
        }
    }
    Row(
        modifier = Modifier
            .graphicsLayer {
                translationY = draggableState.offset * density
                scaleX = (draggableState.offset + 100) * 0.01f
                scaleX = draggableState.offset * 0.005f + 1  // 1 - 0.5
                scaleY = draggableState.offset * 0.005f + 1  // 1 - 0.5
            }
            .anchoredDraggable(
                state = draggableState,
                orientation = Orientation.Vertical,
            )
            .clickableWithoutIndication {
                navController.navigate(route = NavigationRoute.Matches) {
                    popUpTo(navController.graph.id) {
                        inclusive = true
                    }
                }
                onDismiss()
            }
            .fillMaxWidth()
            .background(
                color = MovieTheme.colors.surface.main,
                shape = RoundedCornerShape(16.dp),
            )
            .border(
                width = 1.dp,
                color = MovieTheme.colors.stroke,
                shape = RoundedCornerShape(16.dp),
            )
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = spacedBy(12.dp)
    ) {
        val emoji = remember(Unit) {
            listOf(
                "\uD83D\uDC96",
                "\uD83E\uDD29",
                "\uD83C\uDFAC",
                "\uD83C\uDF7F",
                "\uD83C\uDF89",
            ).random()
        }
        Box(
            modifier = Modifier
                .background(
                    color = MovieTheme.colors.primary.copy(0.25f),
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(8.dp)
                .size(24.dp),
        ) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = emoji,
                style = MovieTheme.typography.title16,
            )
        }
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = spacedBy(2.dp),
        ) {
            Text(
                text = visuals.title,
                style = MovieTheme.typography.title16,
                color = MovieTheme.colors.text.main,
            )
            Text(
                text = visuals.message,
                style = MovieTheme.typography.body14,
                color = MovieTheme.colors.text.main,
            )
        }
        Icon(
            modifier = Modifier
                .size(16.dp)
                .align(Alignment.Top)
                .clickableWithoutIndication(onClick = onDismiss),
            painter = painterResource(Res.drawable.ic_close),
            tint = MovieTheme.colors.icon.variant,
            contentDescription = null
        )
    }
}

@Preview
@Composable
private fun MatchSnackbarPreview() {
    MovieTheme {
        MatchSnackbar(
            visuals = MatchSnackbarVisuals(
                title = "Title",
                message = "Message",
            ),
            navController = rememberNavController(),
            onDismiss = {},
        )
    }
}