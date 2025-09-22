package com.github.freshmorsikov.moviematcher.core.data.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieCastResponse(
    @SerialName("cast") val cast: List<ActorResponse>,
)

@Serializable
data class ActorResponse(
    @SerialName("id") val id: Long,
    @SerialName("name") val name: String,
    @SerialName("character") val character: String,
    @SerialName("profile_path") val profilePath: String?,
    @SerialName("order") val order: Int,
)