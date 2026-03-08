package com.github.freshmorsikov.moviematcher.core.data.api.supabase.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class InsertRoom(
    val code: String,
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
