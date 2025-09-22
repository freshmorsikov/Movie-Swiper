package com.github.freshmorsikov.moviematcher.feature.details.data

import com.github.freshmorsikov.moviematcher.ActorEntity
import com.github.freshmorsikov.moviematcher.ActorEntityQueries
import com.github.freshmorsikov.moviematcher.core.data.api.ApiService
import com.github.freshmorsikov.moviematcher.feature.details.domain.model.Actor

class ActorRepository(
    private val apiService: ApiService,
    private val actorEntityQueries: ActorEntityQueries,
) {

    suspend fun getActorsByMovieId(movieId: Long): List<Actor> {
        val actors = actorEntityQueries.getActorsByMovieId(movieId).executeAsList()
        return if (actors.isEmpty()) {
            val movieCastResult = apiService.getMovieCastByMovieId(movieId = movieId)
            movieCastResult.onSuccess { response ->
                response.cast.take(10).onEach { actorResponse ->
                    val actorEntity = ActorEntity(
                        id = actorResponse.id,
                        name = actorResponse.name,
                        character = actorResponse.character,
                        profilePath = actorResponse.profilePath.orEmpty(),
                        actorOrder = actorResponse.order.toLong(),
                        movieId = movieId,
                    )
                    actorEntityQueries.insert(actorEntity = actorEntity)
                }
            }
            movieCastResult.getOrNull()?.cast?.map { actorResponse ->
                Actor(
                    id = actorResponse.id,
                    name = actorResponse.name,
                    character = actorResponse.character,
                    profilePath = actorResponse.profilePath.orEmpty(),
                    order = actorResponse.order,
                )
            } ?: emptyList()
        } else {
            actors.map {actorResponse ->
                Actor(
                    id = actorResponse.id,
                    name = actorResponse.name,
                    character = actorResponse.character,
                    profilePath = actorResponse.profilePath,
                    order = actorResponse.actorOrder.toInt(),
                )
            }
        }
    }

}