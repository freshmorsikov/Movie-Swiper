package com.github.freshmorsikov.moviematcher.core.data.api

import io.ktor.client.plugins.logging.Logger

object NetworkLogger : Logger {
    override fun log(message: String) {
        println("Ktor: $message")
    }
}