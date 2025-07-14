package com.github.freshmorsikov.moviematcher.core.data.di

import com.github.freshmorsikov.moviematcher.Database
import com.github.freshmorsikov.moviematcher.core.data.api.ApiService
import com.github.freshmorsikov.moviematcher.core.data.api.BASE_URL
import com.github.freshmorsikov.moviematcher.core.data.api.NetworkLogger
import com.github.freshmorsikov.moviematcher.core.data.api.engine
import com.github.freshmorsikov.moviematcher.core.data.local.KeyValueStore
import io.ktor.client.HttpClient
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.URLProtocol
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import moviematcher.composeApp.BuildConfig
import org.koin.dsl.module

val dataModule = module {
    single {
        KeyValueStore(dataStore = get())
    }

    single {
        Database(driver = get())
    }
    factory {
        get<Database>().movieEntityQueries
    }
    factory {
        get<Database>().genreEntityQueries
    }
    factory {
        get<Database>().movieGenreReferenceQueries
    }

    single {
        ApiService(httpClient = get())
    }
    single {
        HttpClient(engine) {
            install(DefaultRequest) {
                header(HttpHeaders.ContentType, ContentType.Application.Json)
                bearerAuth(BuildConfig.AUTH_TOKEN)
                url {
                    protocol = URLProtocol.HTTPS
                    host = BASE_URL
                }
            }

            install(Logging) {
                logger = NetworkLogger
                level = LogLevel.ALL
            }

            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                    }
                )
            }
        }
    }
}