package com.github.freshmorsikov.moviematcher.shared.data

import com.github.freshmorsikov.moviematcher.core.data.api.supabase.SupabaseApiService
import com.github.freshmorsikov.moviematcher.core.data.api.supabase.model.ReactionEntity
import com.github.freshmorsikov.moviematcher.shared.domain.model.ReactionAction

class ReactionRepository(
    private val supabaseApiService: SupabaseApiService,
) {

    suspend fun createReaction(
        userId: String,
        movieId: Long,
        action: ReactionAction,
    ) {
        supabaseApiService.createReaction(
            userId = userId,
            movieId = movieId,
            action = when (action) {
                ReactionAction.Liked -> ReactionEntity.Action.Liked
                ReactionAction.Disliked -> ReactionEntity.Action.Disliked
            },
        )
    }

    suspend fun hasLikedReaction(userId: String, movieId: Long): Boolean {
        return supabaseApiService.getReaction(
            userId = userId,
            movieId = movieId,
            action = ReactionEntity.Action.Liked,
        ) != null
    }

}