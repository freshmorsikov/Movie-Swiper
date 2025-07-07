package com.github.freshmorsikov.moviematcher.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.URLProtocol
import io.ktor.http.isSuccess
import io.ktor.http.path
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import moviematcher.composeApp.BuildConfig

expect val engine: HttpClientEngine

object ApiService {

    private val httpClient = HttpClient(engine) {
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

    @Serializable
    data class PageResponse<T>(
        @SerialName("page") val page: Int,
        @SerialName("results") val results: List<T>
    )

    @Serializable
    data class MovieResponse(
        @SerialName("title") val title: String,
        @SerialName("original_title") val originalTitle: String,
        @SerialName("poster_path") val posterPath: String,
        @SerialName("release_date") val releaseDate: String,
        @SerialName("vote_average") val voteAverage: Float
    )

    @Serializable
    data class ErrorResponse(
        @SerialName("status_message") val statusMessage: String
    )

    suspend fun getMovieList(page: Int): Result<PageResponse<MovieResponse>> {
        return try {
            val response = httpClient.get {
                url {
                    path("discover/movie")
                    parameter("include_adult", false)
                    parameter("include_video", false)
                    parameter("language", "en-US")
                    parameter("page", page)
                    parameter("sort_by", "popularity.desc")
                    parameter("vote_average.gte", 6)
                }
            }
            if (response.status.isSuccess()) {
                Result.success(
                    value = response.body<PageResponse<MovieResponse>>()
                )
            } else {
                val errorMessage = response.body<String>()
                Result.failure(
                    exception = Exception(errorMessage)
                )
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
            Result.failure(exception)
        }
    }

}