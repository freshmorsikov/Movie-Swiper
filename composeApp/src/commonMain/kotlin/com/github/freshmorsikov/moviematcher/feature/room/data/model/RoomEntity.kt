package com.github.freshmorsikov.moviematcher.feature.room.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class InsertRoom(
    val code: String,
    @SerialName("genre_filter")
    val genreFilter: List<Long>,
)

@Serializable
data class UpdateRoomGenreFilter(
    @SerialName("genre_filter")
    val genreFilter: List<Long>,
)

@Serializable
data class RoomEntity(
    val id: String,
    val code: String,
    @SerialName("genre_filter")
    val genreFilter: List<Long>?,
)
