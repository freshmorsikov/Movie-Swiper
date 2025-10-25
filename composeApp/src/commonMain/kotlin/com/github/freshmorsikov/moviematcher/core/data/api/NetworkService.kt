package com.github.freshmorsikov.moviematcher.core.data.api

import androidx.compose.ui.text.intl.Locale
import com.github.freshmorsikov.moviematcher.core.data.api.model.GenreListResponse
import com.github.freshmorsikov.moviematcher.core.data.api.model.MovieCastResponse
import com.github.freshmorsikov.moviematcher.core.data.api.model.MovieDetailsResponse
import com.github.freshmorsikov.moviematcher.core.data.api.model.MovieResponse
import com.github.freshmorsikov.moviematcher.core.data.api.model.PageResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.HttpResponse
import io.ktor.http.isSuccess
import io.ktor.http.path

expect val engine: HttpClientEngine

class ApiService(private val httpClient: HttpClient) {

    suspend fun getMovieList(page: Int): Result<PageResponse<MovieResponse>> {
        return safeApiCall {
            httpClient.get {
                url {
                    path("3/discover/movie")
                    parameter("include_adult", false)
                    parameter("include_video", false)
                    parameter("language", Locale.current.language)
                    parameter("page", page)
                    parameter("sort_by", "popularity.desc")
                    parameter("primary_release_date.lte", "2025-01-01")
                    parameter("vote_average.gte", 6)
                    parameter("vote_count.gte", 200)
                }
            }
        }
    }

    suspend fun getGenreList(): Result<GenreListResponse> {
        return safeApiCall {
            httpClient.get {
                url {
                    path("3/genre/movie/list")
                    parameter("language", Locale.current.language)
                }
            }
        }
    }

    suspend fun getMovieDetailsById(movieId: Long): Result<MovieDetailsResponse> {
        return safeApiCall {
            httpClient.get {
                url {
                    path("3/movie/$movieId")
                    parameter("language", Locale.current.language)
                }
            }
        }
    }

    suspend fun getMovieCastByMovieId(movieId: Long): Result<MovieCastResponse> {
        return safeApiCall {
            httpClient.get {
                url {
                    path("3/movie/$movieId/credits")
                    parameter("language",Locale.current.language)
                }
            }
        }
    }

    private suspend inline fun <reified T>  safeApiCall(
        block: () -> HttpResponse
    ): Result<T> {
        return try {
            val response = block()
            if (response.status.isSuccess()) {
                Result.success(
                    value = response.body<T>()
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