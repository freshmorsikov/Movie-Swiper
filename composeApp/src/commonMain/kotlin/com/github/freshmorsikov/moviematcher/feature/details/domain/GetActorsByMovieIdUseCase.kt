package com.github.freshmorsikov.moviematcher.feature.details.domain

import com.github.freshmorsikov.moviematcher.feature.details.data.ActorRepository
import com.github.freshmorsikov.moviematcher.feature.details.domain.model.Actor

class GetActorsByMovieIdUseCase(
    private val actorRepository: ActorRepository
) {

    suspend operator fun invoke(movieId: Long): List<Actor> {
        return actorRepository.getActorsByMovieId(movieId = movieId)
            .filter { actor ->
                actor.profilePath.isNotEmpty()
            }
            .sortedBy { actor ->
                actor.order
            }
    }

}