package com.github.freshmorsikov.moviematcher.core.data.api

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emptyFlow

suspend inline fun <T> safeCall(
    block: suspend () -> T
): T? {
    return runCatching {
        block()
    }.getOrNull()
}

inline fun <T> safeFlow(
    block: () -> Flow<T>
): Flow<T> {
    return runCatching {
        block().catch {
            // TODO add proper handling
        }
    }.getOrElse {
        emptyFlow()
    }
}