package com.github.freshmorsikov.moviematcher.api

import io.ktor.client.plugins.logging.Logger

object NetworkLogger : Logger {
    override fun log(message: String) {
        println("Ktor: $message")
    }
}