package com.github.freshmorsikov.moviematcher.core.data.api.supabase.model

import kotlinx.serialization.Serializable

@Serializable
data class RoomEntity(
    val id: String,
    val code: String,
)