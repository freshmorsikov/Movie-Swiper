package com.github.freshmorsikov.moviematcher.shared.ui.movie

import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.github.freshmorsikov.moviematcher.util.toRatingFormat
import moviematcher.composeapp.generated.resources.Res
import moviematcher.composeapp.generated.resources.ic_star
import org.jetbrains.compose.resources.painterResource

@Composable
fun MovieInfo(
    releaseDate: String,
    voteAverage: Double,
    voteCount: Int,
    runtime: Int? = null,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = spacedBy(8.dp)
    ) {
        Text(
            text = releaseDate.take(4),
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
        )
        Text(
            text = "|",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = spacedBy(2.dp)
        ) {
            Icon(
                modifier = Modifier.size(14.dp),
                painter = painterResource(Res.drawable.ic_star),
                tint = Color(0xFFEAAF00),
                contentDescription = null
            )
            val voteText = buildAnnotatedString {
                withStyle(SpanStyle(color = Color(0xFFEAAF00))) {
                    append(voteAverage.toRatingFormat())
                }
                if (voteCount > 0) {
                    if (voteCount > 1000) {
                        append(" (${voteCount / 1000}K)")
                    } else {
                        append(" ($voteCount)")
                    }
                }
            }
            Text(
                text = voteText,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
            )
        }
        if (runtime != null) {
            Text(
                text = "|",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
            )
            val runtimeText = buildString {
                val hours = runtime / 60
                if (hours > 0) {
                    append("${hours}h ")
                }

                val minutes = runtime % 60
                if (minutes > 0) {
                    append("${minutes}m")
                }
            }
            Text(
                text = runtimeText,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
            )
        }
    }
}