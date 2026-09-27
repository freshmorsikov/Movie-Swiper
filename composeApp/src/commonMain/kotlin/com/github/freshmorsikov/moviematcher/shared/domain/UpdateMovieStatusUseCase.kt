package com.github.freshmorsikov.moviematcher.shared.domain

import com.github.freshmorsikov.moviematcher.shared.data.MovieRepository
import com.github.freshmorsikov.moviematcher.shared.data.ReactionRepository
import com.github.freshmorsikov.moviematcher.shared.domain.model.MovieStatus
import com.github.freshmorsikov.moviematcher.shared.domain.model.ReactionAction

class UpdateMovieStatusUseCase(
    private val movieRepository: MovieRepository,
    private val reactionRepository: ReactionRepository,
) {

    suspend operator fun invoke(
        id: Long,
        movieStatus: MovieStatus
    ) {
        movieRepository.updateMovieStatus(
            id = id,
            status = movieStatus,
        )
        movieStatus.toReactionAction()?.let { action ->
            reactionRepository.handleReactionAction(
                movieId = id,
                action = action,
            )
        }
    }

    private fun MovieStatus.toReactionAction(): ReactionAction? {
        return when (this) {
            MovieStatus.Liked -> ReactionAction.Liked
            MovieStatus.Disliked -> ReactionAction.Disliked
            else -> null
        }
    }

}
