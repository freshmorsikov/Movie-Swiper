package com.github.freshmorsikov.moviematcher.core.data.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieDetailsResponse(
    @SerialName("id") val id: Long,
    @SerialName("title") val title: String,
    @SerialName("poster_path") val posterPath: String,
    @SerialName("release_date") val releaseDate: String,
    @SerialName("vote_average") val voteAverage: Double,
    @SerialName("vote_count") val voteCount: Long,
    @SerialName("popularity") val popularity: Double,
    @SerialName("overview") val overview: String,
    @SerialName("runtime") val runtime: Long,
    @SerialName("budget") val budget: Long,
    @SerialName("revenue") val revenue: Long,
)