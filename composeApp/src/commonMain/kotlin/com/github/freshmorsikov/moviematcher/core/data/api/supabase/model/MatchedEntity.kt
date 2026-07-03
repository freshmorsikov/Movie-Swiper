package com.github.freshmorsikov.moviematcher.core.data.api.supabase.model

import kotlinx.serialization.Serializable

@Serializable
data class InsertMatched(
    val room: String,
    val movie: Long,
    val active: Boolean,
)

@Serializable
data class UpdateMatchedActive(
    val active: Boolean,
)

@Serializable
data class MatchedEntity(
    val id: String,
    val room: String,
    val movie: Long,
    val active: Boolean,
)
