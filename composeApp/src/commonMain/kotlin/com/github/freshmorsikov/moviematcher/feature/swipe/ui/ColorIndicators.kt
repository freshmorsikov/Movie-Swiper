package com.github.freshmorsikov.moviematcher.feature.swipe.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

enum class IndicatorAlignment {
    Start,
    End,
}

@Composable
fun ColorIndicators(
    dragState: DragState,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        ColorIndicator(
            modifier = Modifier.align(Alignment.CenterStart),
            color = Color(0xFFFF3A31),
            alpha = dragState.negativeColorAlpha.value,
            alignment = IndicatorAlignment.Start
        )
        ColorIndicator(
            modifier = Modifier.align(Alignment.CenterEnd),
            color = Color(0xFF4ED964),
            alpha = dragState.positiveColorAlpha.value,
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
    val startColor = if (alignment == IndicatorAlignment.Start) color.copy(alpha = alpha) else Color.Transparent
    val endColor = if (alignment == IndicatorAlignment.Start) Color.Transparent else color.copy(alpha = alpha)
    Box(
        modifier = modifier
            .fillMaxHeight()
            .width(100.dp)
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(startColor, endColor)
                )
            )
    )
}