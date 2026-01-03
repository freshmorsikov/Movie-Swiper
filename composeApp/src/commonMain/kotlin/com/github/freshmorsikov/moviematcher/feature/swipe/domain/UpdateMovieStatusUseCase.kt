package com.github.freshmorsikov.moviematcher.feature.swipe.domain

import com.github.freshmorsikov.moviematcher.shared.data.MatchRepository
import com.github.freshmorsikov.moviematcher.shared.data.MovieRepository
import com.github.freshmorsikov.moviematcher.shared.domain.GetCodeUseCase
import com.github.freshmorsikov.moviematcher.shared.domain.model.MovieStatus

class UpdateMovieStatusUseCase(
    private val movieRepository: MovieRepository,
    private val getCodeUseCase: GetCodeUseCase,
    private val getRoomUseCase: GetRoomUseCase,
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
                val isDisliked = matchRepository.isMovieDisliked(
                    code = code,
                    movieId = id,
                )
                if (isDisliked) {
                    matchRepository.removeFromDisliked(
                        code = code,
                        movieId = id,
                    )
                } else {
                    val isLiked = matchRepository.isMovieLiked(
                        code = code,
                        movieId = id,
                    )
                    if (isLiked) {
                        matchRepository.removeFromLiked(
                            code = code,
                            movieId = id,
                        )
                        getRoomUseCase()?.let { room ->
                            matchRepository.addToMatched(
                                roomId = room.id,
                                movieId = id,
                            )
                        }
                    } else {
                        matchRepository.addToLiked(
                            code = code,
                            movieId = id,
                        )
                    }
                }
            }

            MovieStatus.Disliked -> {
                val isLiked = matchRepository.isMovieLiked(
                    code = code,
                    movieId = id,
                )
                if (isLiked) {
                    matchRepository.removeFromLiked(
                        code = code,
                        movieId = id,
                    )
                } else {
                    val isDisliked = matchRepository.isMovieDisliked(
                        code = code,
                        movieId = id,
                    )
                    if (isDisliked) {
                        matchRepository.removeFromDisliked(
                            code = code,
                            movieId = id,
                        )
                    } else {
                        matchRepository.addToDisliked(
                            code = code,
                            movieId = id,
                        )
                    }
                }
            }

            else -> Unit
        }
    }

}