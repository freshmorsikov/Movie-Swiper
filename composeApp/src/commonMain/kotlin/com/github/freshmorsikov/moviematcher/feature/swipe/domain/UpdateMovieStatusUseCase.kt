package com.github.freshmorsikov.moviematcher.feature.swipe.domain

import com.github.freshmorsikov.moviematcher.feature.swipe.data.MovieRepository
import com.github.freshmorsikov.moviematcher.feature.swipe.domain.model.MovieStatus
import com.github.freshmorsikov.moviematcher.shared.data.MatchRepository
import com.github.freshmorsikov.moviematcher.shared.domain.GetCodeUseCase

class UpdateMovieStatusUseCase(
    private val movieRepository: MovieRepository,
    private val getCodeUseCase: GetCodeUseCase,
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

        val code = getCodeUseCase()
        when (movieStatus) {
            MovieStatus.Liked -> {
                matchRepository.addToLiked(
                    code = code,
                    movieId = id,
                )
            }

            MovieStatus.Disliked -> {
                matchRepository.addToDisliked(
                    code = code,
                    movieId = id,
                )
            }

            else -> Unit
        }
    }

}