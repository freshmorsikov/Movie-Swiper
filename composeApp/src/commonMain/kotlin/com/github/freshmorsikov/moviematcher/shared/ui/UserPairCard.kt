package com.github.freshmorsikov.moviematcher.shared.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.freshmorsikov.moviematcher.core.ui.theme.MovieTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun UserPairCard(
    leftName: String,
    rightName: String,
    leftEmoji: String,
    rightEmoji: String,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(size = 14.dp),
        colors = CardDefaults.cardColors(
            containerColor = MovieTheme.colors.surface.main
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp),
        ) {
            Spacer(modifier = Modifier.weight(1f))
            PairingProfileItem(
                name = leftName,
                emoji = leftEmoji,
                backgroundColor = MovieTheme.colors.primary,
            )
            HorizontalDivider(
                modifier = Modifier
                    .weight(2f)
                    .padding(
                        top = 24.dp,
                        start = 16.dp,
                        end = 16.dp,
                    ),
                thickness = 2.dp,
                color = MovieTheme.colors.stroke
            )
            PairingProfileItem(
                name = rightName,
                emoji = rightEmoji,
                backgroundColor = MovieTheme.colors.warning,
            )
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Composable
private fun PairingProfileItem(
    name: String,
    emoji: String,
    backgroundColor: Color,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(shape = CircleShape)
                .background(color = backgroundColor),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = emoji,
                fontSize = 24.sp,
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = name,
            style = MovieTheme.typography.title16,
            color = MovieTheme.colors.text.main,
        )
    }
}

@Preview
@Composable
private fun PairingUsersCardPreview() {
    MovieTheme {
        UserPairCard(
            modifier = Modifier.fillMaxWidth(),
            leftName = "Alan",
            rightName = "Kate",
            leftEmoji = "\uD83D\uDC35",
            rightEmoji = "\uD83D\uDC36",
        )
    }
}
