package com.github.freshmorsikov.moviematcher.core.data.api.supabase.model

import kotlinx.serialization.Serializable

@Serializable
data class InsertReaction(
    val user: String,
    val movie: Long,
    val action: ReactionEntity.Action,
)

@Serializable
data class ReactionEntity(
    val id: String,
    val user: String,
    val movie: Long,
    val action: Action,
) {

    @Serializable
    enum class Action {
        Liked,
        Disliked,
    }
}

