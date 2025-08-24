package com.github.freshmorsikov.moviematcher.feature.swipe.domain

import com.github.freshmorsikov.moviematcher.feature.matches.domain.GetPairIdUseCase
import com.github.freshmorsikov.moviematcher.feature.swipe.data.MovieRepository
import com.github.freshmorsikov.moviematcher.feature.swipe.domain.model.MovieStatus
import com.github.freshmorsikov.moviematcher.shared.data.MatchRepository

class UpdateMovieStatusUseCase(
    private val movieRepository: MovieRepository,
    private val getPairIdUseCase: GetPairIdUseCase,
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

        val pairId = getPairIdUseCase()
        when (movieStatus) {
            MovieStatus.Liked -> {
                matchRepository.addToLiked(
                    pairId = pairId,
                    movieId = id,
                )
            }

            MovieStatus.Disliked -> {
                matchRepository.addToDisliked(
                    pairId = pairId,
                    movieId = id,
                )
            }

            else -> Unit
        }
    }

}