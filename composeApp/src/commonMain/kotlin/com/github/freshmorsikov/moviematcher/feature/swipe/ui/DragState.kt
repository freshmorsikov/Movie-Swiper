package com.github.freshmorsikov.moviematcher.feature.swipe.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@Composable
fun rememberDragState(
    key: Any?,
    animationSpec: AnimationSpec<Float>,
): DragState {
    var positiveColorAlpha by remember { mutableFloatStateOf(0f) }
    var negativeColorAlpha by remember { mutableFloatStateOf(0f) }
    val dragState = remember(key) {
        DragState(
            initialPositiveColorAlpha = positiveColorAlpha,
            initialNegativeColorAlpha = negativeColorAlpha,
            animationSpec = animationSpec,
            onAlphaChanged = { positiveAlpha, negativeAlpha ->
                positiveColorAlpha = positiveAlpha
                negativeColorAlpha = negativeAlpha
            },
        )
    }

    LaunchedEffect(key) {
        dragState.resetAlpha()
        positiveColorAlpha = 0f
        negativeColorAlpha = 0f
    }

    return dragState
}

data class DragState(
    val initialPositiveColorAlpha: Float,
    val initialNegativeColorAlpha: Float,
    val animationSpec: AnimationSpec<Float>,
    val onAlphaChanged: (Float, Float) -> Unit,
) {
    val offsetDp = Animatable(0f)
    val positiveColorAlpha = Animatable(initialPositiveColorAlpha)
    val negativeColorAlpha = Animatable(initialNegativeColorAlpha)
    var updateJob: Job? = null
    var resetJob: Job? = null

    suspend fun updateOffset(delta: Float) = coroutineScope {
        if (resetJob?.isActive == true) {
            return@coroutineScope
        }

        updateJob = launch {
            val newOffset = offsetDp.value + delta
            launch {
                offsetDp.animateTo(targetValue = newOffset)
            }

            val newPositiveAlpha = (newOffset / 200f).coerceIn(0f, 0.5f)
            val newNegativeAlpha = -(newOffset / 200f).coerceIn(-0.5f, 0f)
            onAlphaChanged(newPositiveAlpha, newNegativeAlpha)
            launch {
                positiveColorAlpha.animateTo(targetValue = newPositiveAlpha)
                negativeColorAlpha.animateTo(targetValue = newNegativeAlpha)
            }
        }
    }

    suspend fun resetOffset() = coroutineScope {
        updateJob?.join()
        updateJob = null

        resetJob = launch {
            launch {
                offsetDp.animateTo(
                    targetValue = 0f,
                    animationSpec = animationSpec,
                )
            }
            launch {
                resetAlpha()
            }
        }
    }

    suspend fun resetAlpha() {
        if (positiveColorAlpha.value > 0) {
            positiveColorAlpha.animateTo(
                targetValue = 0f,
                animationSpec = animationSpec,
            )
        }
        if (negativeColorAlpha.value > 0) {
            negativeColorAlpha.animateTo(
                targetValue = 0f,
                animationSpec = animationSpec,
            )
        }
    }
}