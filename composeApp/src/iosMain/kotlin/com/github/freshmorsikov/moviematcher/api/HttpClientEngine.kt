package com.github.freshmorsikov.moviematcher.api

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.darwin.Darwin

actual val engine: HttpClientEngine = Darwin.create()