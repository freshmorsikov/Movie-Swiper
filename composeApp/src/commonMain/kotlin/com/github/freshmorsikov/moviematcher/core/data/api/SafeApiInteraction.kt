package com.github.freshmorsikov.moviematcher.core.data.api

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emptyFlow

suspend inline fun <T> safeCall(
    block: suspend () -> T
): T? {
    return runCatching {
        block()
    }.onFailure { exception ->
        println("Remote call: ${exception.message}")
    }.getOrNull()
}

inline fun <T> safeFlow(
    block: () -> Flow<T>
): Flow<T> {
    return runCatching {
        block().catch { exception ->
            println("Remote flow: ${exception.message}")
        }
    }.onFailure { exception ->
        println("Remote flow: ${exception.message}")
    }.getOrElse {
        emptyFlow()
    }
}