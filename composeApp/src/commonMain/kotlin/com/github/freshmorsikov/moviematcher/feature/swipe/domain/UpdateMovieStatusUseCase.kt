package com.github.freshmorsikov.moviematcher.feature.swipe.domain

import com.github.freshmorsikov.moviematcher.shared.data.MatchRepository
import com.github.freshmorsikov.moviematcher.shared.data.MovieRepository
import com.github.freshmorsikov.moviematcher.shared.data.ReactionRepository
import com.github.freshmorsikov.moviematcher.feature.user.data.UserRepository
import com.github.freshmorsikov.moviematcher.shared.domain.model.MovieStatus
import com.github.freshmorsikov.moviematcher.shared.domain.model.ReactionAction

class UpdateMovieStatusUseCase(
    private val movieRepository: MovieRepository,
    private val userRepository: UserRepository,
    private val reactionRepository: ReactionRepository,
    private val matchRepository: MatchRepository,
) {

    suspend operator fun invoke(
        id: Long,
        movieStatus: MovieStatus
    ) {
        movieRepository.updateMovieStatus(
            id = id,
            status = movieStatus,
        )
        movieStatus.toReactionAction()
            ?.let { action ->
                val userId = userRepository.getUserId()
                reactionRepository.createReaction(
                    userId = userId,
                    movieId = id,
                    action = action,
                )
            }
        if (movieStatus == MovieStatus.Liked) {
            val pairedUser = userRepository.getPairedUser() ?: return
            val hasLiked = reactionRepository.hasLikedReaction(
                userId = pairedUser.id,
                movieId = id
            )
            if (hasLiked) {
                matchRepository.addToMatched(
                    roomId = pairedUser.room,
                    movieId = id,
                )
            }
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