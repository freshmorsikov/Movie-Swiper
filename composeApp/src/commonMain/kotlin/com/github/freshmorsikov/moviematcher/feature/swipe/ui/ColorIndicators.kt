package com.github.freshmorsikov.moviematcher.feature.swipe.ui

import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.github.freshmorsikov.moviematcher.feature.swipe.presentation.SwipeUdf.MovieCardState

enum class IndicatorAlignment {
    Start,
    End,
}

@Composable
fun ColorIndicators(
    draggableState: AnchoredDraggableState<MovieCardState>,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        ColorIndicator(
            modifier = Modifier.align(Alignment.CenterStart),
            color = Color(0xFFFF3A31),
            alpha = draggableState
                .progress(MovieCardState.Center, MovieCardState.Swiped.Left)
                .coerceIn(0f, 0.5f),
            alignment = IndicatorAlignment.Start
        )
        ColorIndicator(
            modifier = Modifier.align(Alignment.CenterEnd),
            color = Color(0xFF4ED964),
            alpha = draggableState
                .progress(MovieCardState.Center, MovieCardState.Swiped.Right)
                .coerceIn(0f, 0.5f),
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
            .alpha(alpha = alpha)
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