package com.github.freshmorsikov.moviematcher.app.snackbar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarVisuals
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.github.freshmorsikov.moviematcher.app.navigation.NavigationRoute
import com.github.freshmorsikov.moviematcher.util.clickableWithoutIndication
import moviematcher.composeapp.generated.resources.Res
import moviematcher.composeapp.generated.resources.ic_close
import moviematcher.composeapp.generated.resources.ic_match
import org.jetbrains.compose.resources.painterResource

class MatchSnackbarVisuals(
    val title: String,
    override val message: String,
    override val withDismissAction: Boolean = false,
    override val duration: SnackbarDuration = SnackbarDuration.Short,
    override val actionLabel: String? = null,
) : SnackbarVisuals

@Composable
fun MatchSnackbar(
    visuals: MatchSnackbarVisuals,
    navController: NavController,
    onDismiss: () -> Unit,
) {
    Row(
        modifier = Modifier
            .clickableWithoutIndication {
                navController.navigate(route = NavigationRoute.Matches) {
                    popUpTo(navController.graph.id) {
                        inclusive = true
                    }
                }
                onDismiss()
            }
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(16.dp))
            .border(
                width = 1.dp,
                color = Color.Black.copy(alpha = 0.1f),
                shape = RoundedCornerShape(16.dp),
            )
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = spacedBy(12.dp)
    ) {
        Icon(
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.primary.copy(0.25f),
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(8.dp)
                .size(24.dp),
            painter = painterResource(Res.drawable.ic_match),
            tint = MaterialTheme.colorScheme.primary,
            contentDescription = null
        )
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = spacedBy(2.dp),
        ) {
            Text(
                text = visuals.title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                text = visuals.message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
        Icon(
            modifier = Modifier
                .size(16.dp)
                .align(Alignment.Top)
                .clickableWithoutIndication(onClick = onDismiss),
            painter = painterResource(Res.drawable.ic_close),
            tint = MaterialTheme.colorScheme.secondary,
            contentDescription = null
        )
    }
}