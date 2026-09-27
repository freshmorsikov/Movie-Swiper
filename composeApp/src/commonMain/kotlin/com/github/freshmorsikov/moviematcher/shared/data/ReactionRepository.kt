package com.github.freshmorsikov.moviematcher.shared.data

import com.github.freshmorsikov.moviematcher.core.data.api.supabase.SupabaseApiService
import com.github.freshmorsikov.moviematcher.feature.user.data.UserRepository
import com.github.freshmorsikov.moviematcher.shared.domain.model.ReactionAction

class ReactionRepository(
    private val supabaseApiService: SupabaseApiService,
    private val userRepository: UserRepository,
) {

    suspend fun handleReactionAction(
        movieId: Long,
        action: ReactionAction,
    ) {
        val userId = userRepository.getUserId()
        supabaseApiService.handleReactionAction(
            userId = userId,
            movieId = movieId,
            action = action.name,
        )
    }

}
