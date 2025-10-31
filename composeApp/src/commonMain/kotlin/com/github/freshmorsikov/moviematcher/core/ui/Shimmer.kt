package com.github.freshmorsikov.moviematcher.core.ui

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.freshmorsikov.moviematcher.core.ui.theme.MovieTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ContainerShimmer(
    modifier: Modifier,
    content: @Composable BoxScope.() -> Unit,
) {
    Box(
        modifier = modifier.containerShimmer(),
        content = content,
    )
}

@Composable
fun Shimmer(modifier: Modifier) {
    Spacer(modifier = modifier.contentShimmer())
}

@Composable
private fun Modifier.contentShimmer(): Modifier {
    val alpha by shimmerAlpha(label = "content")
    return background(
        color = MovieTheme.colors.shimmer.content.copy(alpha = alpha),
        shape = RoundedCornerShape(8.dp)
    )
}

@Composable
private fun Modifier.containerShimmer(): Modifier {
    val alpha by shimmerAlpha(label = "container")
    return background(
        color = MovieTheme.colors.shimmer.container.copy(alpha = alpha),
        shape = RoundedCornerShape(8.dp)
    )
}

@Composable
private fun shimmerAlpha(label: String): State<Float> {
    val transition = rememberInfiniteTransition(label = "${label}ShimmerTransition")
    return transition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1000,
                easing = LinearEasing,
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "${label}ShimmerAnimation"
    )
}

@Preview
@Composable
private fun ShimmerPreview() {
    MovieTheme {
        ContainerShimmer(
            modifier = Modifier
                .width(184.dp)
                .height(96.dp)
        ) {
            Shimmer(
                modifier = Modifier
                    .align(Alignment.Center)
                    .width(120.dp)
                    .height(32.dp)
            )
        }
    }
}