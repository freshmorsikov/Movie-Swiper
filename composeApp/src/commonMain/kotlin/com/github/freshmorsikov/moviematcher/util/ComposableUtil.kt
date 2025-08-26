package com.github.freshmorsikov.moviematcher.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.github.freshmorsikov.moviematcher.core.presentation.Udf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Composable
fun <E: Udf.Event> SubscribeOnEvents(
    events: Flow<E>,
    block: (E) -> Unit
) {
    LaunchedEffect(Unit) {
        events.onEach {
            block(it)
        }.launchIn(this)
    }
}