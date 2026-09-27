package com.github.freshmorsikov.moviematcher.core.data.api.supabase.model

import kotlinx.serialization.Serializable

@Serializable
data class HandleReactionActionRequest(
    val userId: String,
    val movieId: Long,
    val action: String,
)
