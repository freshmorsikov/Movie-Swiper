package com.github.freshmorsikov.moviematcher.feature.swipe.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.github.freshmorsikov.moviematcher.feature.swipe.presentation.SwipeUdf.MovieCardState

enum class IndicatorAlignment {
    Start,
    End,
}

@Composable
fun rememberIndicatorAlpha(
    targetMovieCardState: MovieCardState,
    draggableState: AnchoredDraggableState<MovieCardState>,
): Float {
    val alphaAnimatable = remember { Animatable(0f) }
    val progress = draggableState.progress(MovieCardState.Center, targetMovieCardState)
    val alphaTargetValue = progress.coerceIn(0f, 0.5f)
    LaunchedEffect(alphaTargetValue) {
        if (alphaTargetValue == 0f) {
            alphaAnimatable.animateTo(alphaTargetValue, tween(durationMillis = 500))
        } else {
            alphaAnimatable.snapTo(alphaTargetValue)
        }
    }

    return alphaAnimatable.value
}

@Composable
fun ColorIndicators(
    draggableState: AnchoredDraggableState<MovieCardState>,
    modifier: Modifier = Modifier
) {

    Box(modifier = modifier) {
        val leftAlpha = rememberIndicatorAlpha(
            targetMovieCardState = MovieCardState.Swiped.Left,
            draggableState = draggableState,
        )
        ColorIndicator(
            modifier = Modifier.align(Alignment.CenterStart),
            color = Color(0xFFFF3A31),
            alpha = leftAlpha,
            alignment = IndicatorAlignment.Start
        )

        val rightAlpha = rememberIndicatorAlpha(
            targetMovieCardState = MovieCardState.Swiped.Right,
            draggableState = draggableState,
        )
        ColorIndicator(
            modifier = Modifier.align(Alignment.CenterEnd),
            color = Color(0xFF4ED964),
            alpha = rightAlpha,
            alignment = IndicatorAlignment.End
        )
    }
}

@Composable
private fun ColorIndicator(
    alpha: Float,
    color: Color,
    alignment: IndicatorAlignment,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxHeight()
            .width(100.dp)
            .graphicsLayer {
                this.alpha = alpha
            }
            .drawWithCache {
                val startColor = if (alignment == IndicatorAlignment.Start) color else Color.Transparent
                val endColor = if (alignment == IndicatorAlignment.Start) Color.Transparent else color
                val brush = Brush.horizontalGradient(
                    colorStops = arrayOf(
                        0f to startColor,
                        1f to endColor
                    )
                )

                onDrawBehind {
                    drawRect(brush)
                }
            }
    )
}