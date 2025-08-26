package com.github.freshmorsikov.moviematcher.util

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import com.github.freshmorsikov.moviematcher.core.presentation.Udf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Composable
fun <E: Udf.Event> SubscribeOnEvents(
    events: Flow<E>,
    block: suspend (E) -> Unit
) {
    LaunchedEffect(Unit) {
        events.onEach {
            block(it)
        }.launchIn(this)
    }
}

fun Modifier.clickableWithoutIndication(
    onClick: () -> Unit,
) = composed {
    Modifier.clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = null,
        onClick = onClick
    )
}