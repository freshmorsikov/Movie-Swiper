package com.github.freshmorsikov.moviematcher.core.data.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GenreListResponse(
    @SerialName("genres") val genres: List<GenreResponse>
)

@Serializable
data class GenreResponse(
    @SerialName("id") val id: Long,
    @SerialName("name") val name: String
)